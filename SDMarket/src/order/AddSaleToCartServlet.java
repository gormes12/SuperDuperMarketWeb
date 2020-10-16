package order;

import com.google.gson.Gson;
import dto.SaleDTO;
import dto.SaleDetailsDTO;
import my.project.manager.ZoneManager;
import utils.ConstantsUtils;
import utils.ServletUtils;
import utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

@WebServlet(name = "AddSaleToCartServlet", urlPatterns = {"/addSaleToCart"})
public class AddSaleToCartServlet extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("application/json");

        String username = SessionUtils.getUsername(request);
        if (username == null) {
            response.sendRedirect(request.getContextPath() + "/index.html");
        }

        String zoneName = SessionUtils.getChosenZoneName(request);
        if (zoneName == null) {
            response.sendRedirect(request.getContextPath() + "/pages/main/information.html");
        }

        ZoneManager zoneManager = ServletUtils.getSystemManager(getServletContext()).getZone(zoneName);

        /*BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
        String json = "";
        if(br != null){
            json = br.readLine();
            System.out.println(json);
        }*/

        String json = request.getParameter("data");

        Gson gson = new Gson();
        SaleDTOandItemSR saleDTOandItemSR = gson.fromJson(json, SaleDTOandItemSR.class);

        String serialNumber = saleDTOandItemSR.serialNumberItem;
        HashMap<SaleDTO, Integer> salesCart = (HashMap<SaleDTO, Integer>) request.getSession(false).getAttribute(ConstantsUtils.CURRENT_SELECTED_SALES_CART);

        List<SaleDetailsDTO> saleDetails;
        if ((serialNumber == null) || (serialNumber.isEmpty())){
            salesCart.put(saleDTOandItemSR.sale, 0);
            saleDetails = saleDTOandItemSR.sale.getDetails();
        } else{
            Integer i = Integer.parseInt(serialNumber);
            salesCart.put(saleDTOandItemSR.sale, i);
            saleDetails = new LinkedList<>();
            saleDetails.add(saleDTOandItemSR.sale.getSpecificSaleDetailsOnItemFromSale(i));
        }

        request.getSession(false).setAttribute(ConstantsUtils.CURRENT_SELECTED_SALES_CART, salesCart);

        String jsonResponse = gson.toJson(saleDetails);

        try (PrintWriter out = response.getWriter()) {
            out.print(jsonResponse);
            out.flush();
        }
    }

    private static class SaleDTOandItemSR {

        private SaleDTO sale;
        private String serialNumberItem;

        public SaleDTOandItemSR() {

        }

        public SaleDTO getSale() {
            return sale;
        }

        public void setSale(SaleDTO sale) {
            this.sale = sale;
        }

        public String getSerialNumberItem() {
            return serialNumberItem;
        }

        public void setSerialNumberItem(String serialNumberItem) {
            this.serialNumberItem = serialNumberItem;
        }
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


