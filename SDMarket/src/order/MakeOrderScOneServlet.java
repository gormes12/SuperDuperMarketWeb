package order;

import com.google.gson.Gson;
import dto.ItemDTO;
import dto.SuperMarketItemDTO;
import my.project.location.Location;
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
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

@WebServlet(name = "MakeOrderScOneServlet", urlPatterns = {"/makeOrderScOne"})
public class MakeOrderScOneServlet extends HttpServlet {
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

        ZoneManager zoneManager = ServletUtils.getSystemManager(getServletContext()).getZone(zoneName);

        try (PrintWriter out = response.getWriter()) {


            int xCoordinate = Integer.parseInt(request.getParameter("xCoordinate"));
            int yCoordinate = Integer.parseInt(request.getParameter("yCoordinate"));

            Location location = new Location(xCoordinate, yCoordinate);
            if (zoneManager.isLocationCaughtUp(location)) {
                response.setStatus(500);
                out.print("This location caught up by store and you can't insert this location");
                out.flush();
                return;
            } else {
                request.getSession(false).setAttribute(ConstantsUtils.CURRENT_LOCATION, location);
            }

        /*
        Synchronizing as minimum as I can to fetch only the relevant information from the chat manager and then only processing and sending this information onward
        Note that the synchronization here is on the ServletContext, and the one that also synchronized on it is the chat servlet when adding new chat lines.
         */
            LocalDate date = LocalDate.parse(request.getParameter("date-to-order"));
            request.getSession(false).setAttribute(ConstantsUtils.DATE_ORDER, date);

            //create shopping cart
            request.getSession(false).setAttribute(ConstantsUtils.CURRENT_ITEMS_CART, new HashMap<>());


            Gson gson = new Gson();
            String jsonResponse;

            if (request.getParameter("order-type").equals("static")) {
                request.getSession(false).setAttribute(ConstantsUtils.CURRENT_TYPE_ORDER, "static");
                int storeID = Integer.parseInt(request.getParameter("chosenStore"));
                request.getSession(false).setAttribute(ConstantsUtils.CHOSEN_STORE_FOR_STATIC_ORDER, storeID);

                List<SuperMarketItemDTO> itemsEntries;
                synchronized (getServletContext()) {
                    itemsEntries = zoneManager.getAvailableItemFrom(storeID);
                }
                // create the response json string
                jsonResponse = gson.toJson(itemsEntries);
            } else {
                request.getSession(false).setAttribute(ConstantsUtils.CURRENT_TYPE_ORDER, "dynamic");
                List<ItemDTO> itemsEntries;
                synchronized (getServletContext()) {
                    itemsEntries = zoneManager.getItems();
                }

                jsonResponse = gson.toJson(itemsEntries);
            }

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
