package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.datamanager.ContestsManager;
import logic.datamanager.TeamsManager;
import servlets.utils.ServletUtils;
import servlets.utils.SessionUtils;

import java.io.IOException;

@WebServlet(name = "UBoatReadyServlet", urlPatterns = "/contestManager/setReady")
public class UBoatReadyServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String userName = SessionUtils.getUsername(request);
        ContestsManager manager = ServletUtils.getContestManager(request.getServletContext());

        if (userName == null) {
            ServletUtils.createResponse(response, HttpServletResponse.SC_UNAUTHORIZED, null);
            //resp.sendRedirect(); -> want to send redirect to login servlet/page.
            return;
        }
        try{
            String encryptedMessage = request.getParameter("encryptedMessage");
            if(manager.isUserExists(userName)){
                manager.setEncryptedMessage(userName, encryptedMessage);
                ServletUtils.createResponse(response, HttpServletResponse.SC_OK, null);
            }
            else {
                ServletUtils.createResponse(response, HttpServletResponse.SC_CONFLICT, null);
                // need to explain why in response, maybe you already in the contest/for the uBoat still not load contest ....
                // need to check also that the allie client not in other contest.
            }
        } catch (Exception e){
            System.out.println("There is not a body for this request");
        }

    }
}