package alert;

import com.google.gson.Gson;
import dto.OrderDTO;
import dto.ShoppingCartDTO;
import dto.StoreDTO;
import dto.ZoneDTO;
import my.project.manager.OrderManager;
import my.project.manager.SystemManager;
import my.project.manager.ZonesManager;
import my.project.user.StoreOwner;
import my.project.user.User;
import utils.ConstantsUtils;
import utils.ServletUtils;
import utils.SessionUtils;
import utils.ThreadSafeUtils;
import zones.ZonesServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "getOrderAlertServlet", urlPatterns = {"/getOrderAlert"})
public class getOrderAlertServlet extends HttpServlet {
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
        int orderAlertVersion = (int) request.getSession(false).getAttribute(ConstantsUtils.ORDER_ALERT_VERSION_PARAMETER);
        if (orderAlertVersion == ConstantsUtils.INT_PARAMETER_ERROR) {
            return;
        }

        StoreOwner storeOwner = (StoreOwner) ServletUtils.getSystemManager(request.getServletContext()).getUserManager().getUser(username);
        OrderManager storeOwnerOrderManager = storeOwner.getOrderManager();

        /*
        Synchronizing as minimum as I can to fetch only the relevant information from the chat manager and then only processing and sending this information onward
        Note that the synchronization here is on the ServletContext, and the one that also synchronized on it is the chat servlet when adding new chat lines.
         */
        int orderAlertStoreOwnerVersion = 0;
        List<ShoppingCartDTO> ordersEntries;
        synchronized (ThreadSafeUtils.orderManagerLock) {
            orderAlertStoreOwnerVersion = storeOwnerOrderManager.getVersion();
            ordersEntries = storeOwnerOrderManager.getOrdersEntries(orderAlertVersion);
        }

        if (orderAlertStoreOwnerVersion == orderAlertVersion){
            return;
        } else {
            request.getSession(false).setAttribute(ConstantsUtils.ORDER_ALERT_VERSION_PARAMETER, orderAlertStoreOwnerVersion);
        }

        // log and create the response json string
//        OrderAlertAndVersion cav = new OrderAlertAndVersion(ordersEntries, orderAlertStoreOwnerVersion);
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(ordersEntries);
        logServerMessage("Server Zones version: " + orderAlertStoreOwnerVersion + ", User '" + username + "' Zone version: " + orderAlertVersion);
        logServerMessage(jsonResponse);

        try (PrintWriter out = response.getWriter()) {
            out.print(jsonResponse);
            out.flush();
        }

    }

    private void logServerMessage(String message){
        System.out.println(message);
    }

    /*private static class OrderAlertAndVersion {

        final private List<ShoppingCartDTO> entries;
        final private int version;

        public OrderAlertAndVersion(List<ShoppingCartDTO> entries, int version) {
            this.entries = entries;
            this.version = version;
        }
    }*/

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
