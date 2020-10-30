package file;

import my.project.manager.SystemManager;
import my.project.manager.ZoneManager;
import my.project.user.User;
import my.project.xml.XMLoader;
import utils.ServletUtils;
import utils.SessionUtils;
import utils.ThreadSafeUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.SequenceInputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

@WebServlet(name = "UploadFileServlet", urlPatterns = {"/uploadfile"})
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class UploadFileServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");

        String user = SessionUtils.getUsername(request);
        SystemManager systemManager = ServletUtils.getSystemManager(getServletContext());

        if (systemManager.getUserType(user) == User.eUserType.StoreOwner) {

            Collection<Part> parts = request.getParts();
            Collection<InputStream> fileInputStreamList = new LinkedList<>();

            for (Part part : parts) {
                //to write the content of the file to a string
                fileInputStreamList.add(part.getInputStream());
            }

            SequenceInputStream fileInputStream =  new SequenceInputStream(Collections.enumeration(fileInputStreamList));
                PrintWriter out = response.getWriter();

            try {
                synchronized (ThreadSafeUtils.zonesManagerLock) {
                    ZoneManager zoneManager = XMLoader.getZoneFrom(fileInputStream, user);
                    systemManager.addZone(zoneManager.getZoneName(), zoneManager);
                }
            } catch (Exception e) {
//                response.setProperty(response.HTTP_STATUS_CODE, "500");
                response.setStatus(500);
                out.print(e.getMessage());
                out.flush();
            }
        } else {
            return;
        }


    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">

    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
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
