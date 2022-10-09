package servlets.utils;

import logic.ContestsManager;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class servletUtils {

    private static final String CONTEST_MANAGER_ATTRIBUTE_NAME = "contestManager";
    private static final Object contestManagerLock = new Object();

    // getters for all the details we should respond for the clients.
    public static ContestsManager getContestManager(ServletContext servletContext){

        synchronized (contestManagerLock){
            if(servletContext.getAttribute(CONTEST_MANAGER_ATTRIBUTE_NAME) == null){
                servletContext.setAttribute(CONTEST_MANAGER_ATTRIBUTE_NAME,new ContestsManager());
            }
        }

        return (ContestsManager)servletContext.getAttribute(CONTEST_MANAGER_ATTRIBUTE_NAME);
    }

    public static void createResponse(HttpServletResponse resp, int status, String msg){

        resp.setContentType("text/plain;charset=UTF-8");
        resp.setStatus(status);
        try {
            resp.getOutputStream().print(msg);
            resp.setContentLength(msg.length());
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
