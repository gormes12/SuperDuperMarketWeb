package item;

import com.google.gson.Gson;
import my.project.manager.SystemManager;
import my.project.manager.ZoneManager;
import utils.ServletUtils;
import utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "ExecuteAddNewItemServlet", urlPatterns = {"/executeAddNewItemToZone"})
public class ExecuteAddNewItemServlet extends HttpServlet {
    private void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

//        response.setContentType("text/html;charset=UTF-8");

        String username = SessionUtils.getUsername(request);
        if (username == null) {
            response.sendRedirect(request.getContextPath() + "/index.html");
        }

        String zoneName = SessionUtils.getChosenZoneName(request);
        if (zoneName == null) {
            response.sendRedirect(request.getContextPath() + "/pages/main/information.html");
        }

        ZoneManager zoneManager = ServletUtils.getSystemManager(getServletContext()).getZone(zoneName);

        String json = request.getParameter("data");

        Gson gson = new Gson();
        ItemData newItemData = gson.fromJson(json, ItemData.class);
        int storeID;
        double itemPrice;

        int itemSerialNumber = zoneManager.addItemAndGetSerialNumber(newItemData.itemName, newItemData.purchaseCategory);
        for (StoreIdAndPrice storeIdAndPrice : newItemData.selectedStoresIdAndPriceList) {
            storeID = Integer.parseInt(storeIdAndPrice.id);
            itemPrice = Double.parseDouble(storeIdAndPrice.price);
            zoneManager.addItemToStore(storeID, itemSerialNumber, itemPrice);
        }

        SystemManager.isInnerInfoChangedInSomeZone = true;
        return;

            /*Gson gson = new Gson();
            String jsonResponse;

            jsonResponse = gson.toJson(orderInProcess.createOrderDTO());
            out.print(jsonResponse);
            out.flush();*/

            /*} catch (Exception e) {
                response.setStatus(500);
                out.print(e.getMessage());
                out.flush();
            }*/
//        }
    }

    private class ItemData {

        private String itemName;
        private String purchaseCategory;
        private List<StoreIdAndPrice> selectedStoresIdAndPriceList;


        public ItemData() {

        }

        public List<StoreIdAndPrice> getSelectedStoresIdAndPriceList() {
            return selectedStoresIdAndPriceList;
        }

        public void setSelectedStoresIdAndPriceList(List<StoreIdAndPrice> selectedStoresIdAndPriceList) {
            this.selectedStoresIdAndPriceList = selectedStoresIdAndPriceList;
        }

        public String getItemName() {
            return itemName;
        }

        public void setItemName(String itemName) {
            this.itemName = itemName;
        }

        public String getPurchaseCategory() {
            return purchaseCategory;
        }

        public void setPurchaseCategory(String purchaseCategory) {
            this.purchaseCategory = purchaseCategory;
        }
    }

    private class StoreIdAndPrice {

        private String id;
        private String price;

        public StoreIdAndPrice() {

        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
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
