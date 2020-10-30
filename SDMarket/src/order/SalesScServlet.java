package order;

import com.google.gson.Gson;
import dto.SaleDTO;
import my.project.location.Location;
import my.project.manager.ZoneManager;
import my.project.order.Order;
import my.project.user.Customer;
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
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;



@WebServlet(name = "SalesScServlet", urlPatterns = {"/sales"})
public class SalesScServlet extends HttpServlet {
    private void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");

        String username = SessionUtils.getUsername(request);
        if (username == null) {
            response.sendRedirect(request.getContextPath() + "/index.html");
        }

        Customer customer = (Customer) ServletUtils.getSystemManager(request.getServletContext()).getUserManager().getUser(username);

        String zoneName = SessionUtils.getChosenZoneName(request);
        if (zoneName == null) {
            response.sendRedirect(request.getContextPath() + "/pages/main/information.html");
        }

        ZoneManager zoneManager = ServletUtils.getSystemManager(getServletContext()).getZone(zoneName);

        try (PrintWriter out = response.getWriter()) {

            Location location = (Location) request.getSession(false).getAttribute(ConstantsUtils.CURRENT_LOCATION);
            customer.setLocation(location);

            LocalDate date = (LocalDate) request.getSession(false).getAttribute(ConstantsUtils.DATE_ORDER);

            HashMap<Integer, Double> shoppingCart = (HashMap<Integer, Double>) request.getSession(false).getAttribute(ConstantsUtils.CURRENT_ITEMS_CART);

            Gson gson = new Gson();
            String jsonResponse;

            Order order;
            List<SaleDTO> salesEntries;
            try {
                if (request.getSession(false).getAttribute(ConstantsUtils.CURRENT_TYPE_ORDER).equals("static")) {
                    int storeID = (Integer) request.getSession(false).getAttribute(ConstantsUtils.CHOSEN_STORE_FOR_STATIC_ORDER);

                    synchronized (ThreadSafeUtils.orderManagerLock) {
                        order = zoneManager.createOrderFromItemList(date, storeID, customer, shoppingCart);
                        salesEntries = zoneManager.getSalesOfActiveOrder();
                    }
                    // create the response json string
                } else {
                    synchronized (ThreadSafeUtils.orderManagerLock) {
                        order = zoneManager.createMinOrderFromItemList(date, customer, shoppingCart);
                        salesEntries = zoneManager.getSalesOfActiveOrder();
                    }
                }

                // SAVE ORDER BEFORE ADD SALES ON SESSION.
                SessionUtils.saveOrderInProcess(request, order);

                // CREATE TEMP SALES CART
                request.getSession(false).setAttribute(ConstantsUtils.CURRENT_SELECTED_SALES_CART, new HashMap<>());

                jsonResponse = gson.toJson(salesEntries);
                out.print(jsonResponse);
                out.flush();

            } catch (Exception e) {
                response.setStatus(500);
                out.print(e.getMessage());
                out.flush();
            }
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
