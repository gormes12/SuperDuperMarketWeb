package alert;

import com.google.gson.Gson;
import dto.ShoppingCartDTO;
import feedback.Feedback;
import my.project.manager.FeedbackManager;
import my.project.manager.OrderManager;
import my.project.user.StoreOwner;
import my.project.user.User;
import utils.ConstantsUtils;
import utils.ServletUtils;
import utils.SessionUtils;
import utils.ThreadSafeUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "getFeedbackAlertServlet", urlPatterns = {"/getFeedbackAlert"})
public class getFeedbackAlertServlet extends HttpServlet {
    private void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");

        String username = SessionUtils.getUsername(request);
        if (username == null) {
            response.sendRedirect(request.getContextPath() + "/index.html");
        }

        if (ServletUtils.getSystemManager(request.getServletContext()).getUserType(username).equals(User.eUserType.Customer)){
            return;
        }

        /*
        verify chat version given from the user is a valid number. if not it is considered an error and nothing is returned back
         */
        int feedbackAlertVersion = (int) request.getSession(false).getAttribute(ConstantsUtils.FEEDBACK_ALERT_VERSION_PARAMETER);
        if (feedbackAlertVersion == ConstantsUtils.INT_PARAMETER_ERROR) {
            return;
        }

        StoreOwner storeOwner = (StoreOwner) ServletUtils.getSystemManager(request.getServletContext()).getUserManager().getUser(username);
        FeedbackManager storeOwnerFeedbacksManager = storeOwner.getFeedbacksManager();

        /*
        Synchronizing as minimum as I can to fetch only the relevant information from the chat manager and then only processing and sending this information onward
        Note that the synchronization here is on the ServletContext, and the one that also synchronized on it is the chat servlet when adding new chat lines.
         */
        int feedbackAlertStoreOwnerVersion = 0;
        List<Feedback> feedbacksEntries;
        synchronized (ThreadSafeUtils.feedbackManagerLock) {
            feedbackAlertStoreOwnerVersion = storeOwnerFeedbacksManager.getVersion();
            feedbacksEntries = storeOwnerFeedbacksManager.getFeedbacksEntries(feedbackAlertVersion);
        }

        if (feedbackAlertStoreOwnerVersion == feedbackAlertVersion){
            return;
        } else {
            request.getSession(false).setAttribute(ConstantsUtils.FEEDBACK_ALERT_VERSION_PARAMETER, feedbackAlertStoreOwnerVersion);
        }

        // create the response json string
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(feedbacksEntries);


        try (PrintWriter out = response.getWriter()) {
            out.print(jsonResponse);
            out.flush();
        }

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
