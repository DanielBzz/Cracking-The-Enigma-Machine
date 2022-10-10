package servlets.utils;

import logic.ContestsManager;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.IOException;
import java.util.Collection;

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

    public static String getBody(HttpServletRequest req){

        Collection<Part> fileParts = null;
        try {
            fileParts = req.getParts();
        } catch (IOException | ServletException e) {
            throw new RuntimeException(e);
        }

        StringBuilder fileContent = new StringBuilder();
        fileParts.forEach(part -> fileContent.append(part.getContentType()));

        return fileContent.toString();
    }
}
