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

@WebServlet(name = "SummaryOrderScServlet", urlPatterns = {"/summaryOrder"})
public class SummaryOrderScServlet extends HttpServlet {
    private void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");

        String username = SessionUtils.getUsername(request);
        if (username == null) {
            response.sendRedirect(request.getContextPath() + "/index.html");
        }

//        Customer customer = (Customer) ServletUtils.getSystemManager(request.getServletContext()).getUserManager().getUser(username);

        String zoneName = SessionUtils.getChosenZoneName(request);
        if (zoneName == null) {
            response.sendRedirect(request.getContextPath() + "/pages/main/information.html");
        }

        ZoneManager zoneManager = ServletUtils.getSystemManager(getServletContext()).getZone(zoneName);

        try (PrintWriter out = response.getWriter()) {

            Order orderInProcess = SessionUtils.getOrderInProcess(request);
            HashMap<SaleDTO, Integer> salesCart = (HashMap<SaleDTO, Integer>) request.getSession(false).getAttribute(ConstantsUtils.CURRENT_SELECTED_SALES_CART);

            if (!salesCart.isEmpty()) {
                // ADD SALES TO ORDER AND SAVE ON SESSION
                orderInProcess = zoneManager.addChosenSaleItemsToCart(salesCart, orderInProcess);
                SessionUtils.saveOrderInProcess(request, orderInProcess);
            }

            Gson gson = new Gson();
            String jsonResponse;

            jsonResponse = gson.toJson(orderInProcess.createOrderDTO());
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
