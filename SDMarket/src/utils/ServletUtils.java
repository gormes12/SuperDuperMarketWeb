package utils;

import my.project.manager.ChatManager;
import my.project.manager.SystemManager;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import static utils.ConstantsUtils.INT_PARAMETER_ERROR;

public class ServletUtils {

    private static final String SYSTEM_MANAGER_ATTRIBUTE_NAME = "systemManager";
    private static final String CHAT_MANAGER_ATTRIBUTE_NAME = "chatManager";

    /*
    Note how the synchronization is done only on the question and\or creation of the relevant managers and once they exists -
    the actual fetch of them is remained un-synchronized for performance POV
     */
    private static final Object systemManagerLock = new Object();
    private static final Object chatManagerLock = new Object();

    public static SystemManager getSystemManager(ServletContext servletContext) {

        synchronized (systemManagerLock) {
            if (servletContext.getAttribute(SYSTEM_MANAGER_ATTRIBUTE_NAME) == null) {
                servletContext.setAttribute(SYSTEM_MANAGER_ATTRIBUTE_NAME, new SystemManager());
            }
        }
        return (SystemManager) servletContext.getAttribute(SYSTEM_MANAGER_ATTRIBUTE_NAME);
    }

    public static ChatManager getChatManager(ServletContext servletContext) {
        synchronized (chatManagerLock) {
            if (servletContext.getAttribute(CHAT_MANAGER_ATTRIBUTE_NAME) == null) {
                servletContext.setAttribute(CHAT_MANAGER_ATTRIBUTE_NAME, new ChatManager());
            }
        }
        return (ChatManager) servletContext.getAttribute(CHAT_MANAGER_ATTRIBUTE_NAME);
    }

    public static int getIntParameter(HttpServletRequest request, String name) {
        String value = request.getParameter(name);
        if (value != null) {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException numberFormatException) {
            }
        }
        return INT_PARAMETER_ERROR;
    }
}
