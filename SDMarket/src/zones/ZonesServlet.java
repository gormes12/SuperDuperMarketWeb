package zones;

import com.google.gson.Gson;
import dto.ZoneDTO;
import my.project.manager.SystemManager;
import my.project.manager.ZonesManager;
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
import java.util.List;

@WebServlet(name = "ZonesServlet", urlPatterns = {"/zones"})
public class ZonesServlet extends HttpServlet {

    private void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        ZonesManager zonesManager = ServletUtils.getSystemManager(getServletContext()).getZonesManager();

        String username = SessionUtils.getUsername(request);
        if (username == null) {
            response.sendRedirect(request.getContextPath() + "/index.html");
        }

        /*
        verify chat version given from the user is a valid number. if not it is considered an error and nothing is returned back
        Obviously the UI should be ready for such a case and handle it properly
         */
        int zonesVersion = ServletUtils.getIntParameter(request, ConstantsUtils.ZONE_VERSION_PARAMETER);
        if (zonesVersion == ConstantsUtils.INT_PARAMETER_ERROR) {
            return;
        }

        /*
        Synchronizing as minimum as I can to fetch only the relevant information from the chat manager and then only processing and sending this information onward
        Note that the synchronization here is on the ServletContext, and the one that also synchronized on it is the chat servlet when adding new chat lines.
         */
        int zonesManagerVersion = 0;
        List<ZoneDTO> zoneEntries;
        synchronized (getServletContext()) {
            zonesManagerVersion = zonesManager.getVersion();
            if (SystemManager.isInnerInfoChangedInSomeZone){
                zoneEntries = zonesManager.getZonesEntries(0);
                SystemManager.isInnerInfoChangedInSomeZone = false;
            } else {
                zoneEntries = zonesManager.getZonesEntries(zonesVersion);
            }
        }

        // log and create the response json string
        ZonesAndVersion cav = new ZonesAndVersion(zoneEntries, zonesManagerVersion);
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(cav);
        logServerMessage("Server Zones version: " + zonesManagerVersion + ", User '" + username + "' Zone version: " + zonesVersion);
        logServerMessage(jsonResponse);

        try (PrintWriter out = response.getWriter()) {
            out.print(jsonResponse);
            out.flush();
        }

    }

    private void logServerMessage(String message){
        System.out.println(message);
    }

    private static class ZonesAndVersion {

        final private List<ZoneDTO> entries;
        final private int version;

        public ZonesAndVersion(List<ZoneDTO> entries, int version) {
            this.entries = entries;
            this.version = version;
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
