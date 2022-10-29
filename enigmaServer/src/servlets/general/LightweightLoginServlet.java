package servlets.general;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.datamanager.DataManager;
import servlets.utils.ServletUtils;
import servlets.utils.SessionUtils;

import java.io.IOException;

import static servlets.utils.SessionUtils.ACCESS_ATTRIBUTE;
import static servlets.utils.SessionUtils.USERNAME_ATTRIBUTE;

@WebServlet(name = "LoginServlet", urlPatterns = "/login")
public class LightweightLoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String usernameFromSession = SessionUtils.getUsername(request);
        try {
            DataManager userManager = ServletUtils.getDataManager(getServletContext(),request.getParameter(ACCESS_ATTRIBUTE) );

            if (usernameFromSession == null) { //user is not logged in yet

                String usernameFromParameter = request.getParameter(USERNAME_ATTRIBUTE);
                if (usernameFromParameter == null || usernameFromParameter.isEmpty()) {
                    response.setStatus(HttpServletResponse.SC_CONFLICT);
                } else {
                    usernameFromParameter = usernameFromParameter.trim();

                    if (userManager.addUser(usernameFromParameter)) {
                        request.getSession(true).setAttribute(USERNAME_ATTRIBUTE, usernameFromParameter);
                        request.getSession(true).setAttribute(ACCESS_ATTRIBUTE,request.getParameter(ACCESS_ATTRIBUTE));
                        System.out.println("On login, request URI is: " + request.getRequestURI() + usernameFromParameter);
                        response.setStatus(HttpServletResponse.SC_OK);
                    } else {
                        String errorMessage = "Username " + usernameFromParameter + " already exists. Please enter a different username.";
                        System.out.println(errorMessage);
                        ServletUtils.createResponse(response,HttpServletResponse.SC_UNAUTHORIZED,errorMessage);
                    }
                }
            } else {
                System.out.println("User is already exist");
                response.setStatus(HttpServletResponse.SC_OK);
            }
        }
        catch (Exception e){
            ServletUtils.createResponse(response,HttpServletResponse.SC_BAD_REQUEST,e.getMessage());
        }

    }

}