package servlets.utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class SessionUtils {

    public static final String USERNAME_ATTRIBUTE = "username";
    public static final String ACCESS_ATTRIBUTE = "access";


    public static String getUsername (HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Object sessionAttribute = session != null ? session.getAttribute(USERNAME_ATTRIBUTE) : null;
        return sessionAttribute != null ? sessionAttribute.toString() : null;
    }

    public static String getAccess (HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Object sessionAttribute = session != null ? session.getAttribute(ACCESS_ATTRIBUTE) : null;
        return sessionAttribute != null ? sessionAttribute.toString() : null;
    }

    public static void clearSession (HttpServletRequest request) {

        request.getSession().removeAttribute(USERNAME_ATTRIBUTE);
        request.getSession().removeAttribute(ACCESS_ATTRIBUTE);
    }
}
