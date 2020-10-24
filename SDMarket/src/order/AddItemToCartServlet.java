package order;

import my.project.manager.ZoneManager;
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
import java.util.HashMap;

@WebServlet(name = "AddItemToCartServlet", urlPatterns = {"/addItemToCart"})
public class AddItemToCartServlet extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        String username = SessionUtils.getUsername(request);
        if (username == null) {
            response.sendRedirect(request.getContextPath() + "/index.html");
        }

        String zoneName = SessionUtils.getChosenZoneName(request);
        if (zoneName == null) {
            response.sendRedirect(request.getContextPath() + "/pages/main/information.html");
        }

        ZoneManager zoneManager = ServletUtils.getSystemManager(getServletContext()).getZone(zoneName);

        Integer serialNumber = Integer.parseInt(request.getParameter("serialnumber"));
        Double amount = Double.parseDouble(request.getParameter("amount"));

        if(zoneManager.getPurchaseCategoryOfItem(serialNumber).equals("Quantity")){
            if (amount % 1 != 0) {
                try (PrintWriter out = response.getWriter()) {
                    response.setStatus(500);
                    out.print("must insert Integer Number");
                    out.flush();
                    return;
                }
            }
        }

        HashMap<Integer, Double> shoppingCart = (HashMap<Integer, Double>) request.getSession(false).getAttribute(ConstantsUtils.CURRENT_ITEMS_CART);
        if (shoppingCart == null) {
            shoppingCart = new HashMap<>();
        }



        Double prevAmount = shoppingCart.putIfAbsent(serialNumber, amount);
        if (prevAmount != null) {
            shoppingCart.put(serialNumber, amount + prevAmount);
        }

        request.getSession(false).setAttribute(ConstantsUtils.CURRENT_ITEMS_CART, shoppingCart);

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
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
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
