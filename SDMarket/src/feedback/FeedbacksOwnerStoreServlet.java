package feedback;

import com.google.gson.Gson;
import dto.OrderDTO;
import my.project.manager.ZoneManager;
import my.project.order.Order;
import my.project.user.Customer;
import my.project.user.StoreOwner;
import utils.ServletUtils;
import utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@WebServlet(name = "FeedbacksOwnerStoreServlet", urlPatterns = {"/getFeedbacks"})
public class FeedbacksOwnerStoreServlet extends HttpServlet {
    private void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");

        String username = SessionUtils.getUsername(request);
        if (username == null) {
            response.sendRedirect(request.getContextPath() + "/index.html");
        }

        String zoneName = SessionUtils.getChosenZoneName(request);
        if (zoneName == null) {
            response.sendRedirect(request.getContextPath() + "/pages/main/information.html");
        }

        StoreOwner storeOwner = (StoreOwner) ServletUtils.getSystemManager(request.getServletContext()).getUserManager().getUser(username);

        /*
        Synchronizing as minimum as I can to fetch only the relevant information from the chat manager and then only processing and sending this information onward
        Note that the synchronization here is on the ServletContext, and the one that also synchronized on it is the chat servlet when adding new chat lines.
         */
        Collection<Feedback> feedbacks;
        synchronized (getServletContext()) {
            feedbacks = storeOwner.getFeedbacksManager().getFeedbackFromZone(zoneName);
        }

        try (PrintWriter out = response.getWriter()) {
            // create the response json string
            Gson gson = new Gson();
            String jsonResponse = gson.toJson(feedbacks);

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
