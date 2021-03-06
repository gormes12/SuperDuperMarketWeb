package utils;

import my.project.order.Order;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class SessionUtils {

    public static String getUsername (HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Object sessionAttribute = session != null ? session.getAttribute(ConstantsUtils.USERNAME) : null;
        return sessionAttribute != null ? sessionAttribute.toString() : null;
    }

    public static String getChosenZoneName(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Object sessionAttribute = session != null ? session.getAttribute(ConstantsUtils.CHOOSE_ZONE_PARAMETER) : null;
        return sessionAttribute != null ? sessionAttribute.toString() : null;
    }

    public static void saveOrderInProcess(HttpServletRequest request, Order order){
        request.getSession(false).setAttribute(ConstantsUtils.CURRENT_ITEMS_CART, order);
    }

    public static Order getOrderInProcess(HttpServletRequest request){
        return (Order) request.getSession(false).getAttribute(ConstantsUtils.CURRENT_ITEMS_CART);
    }

    public static void clearSession (HttpServletRequest request) {
        request.getSession().invalidate();
    }
}
