package servlets.general;

import contestDtos.ActivePlayerDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.datamanager.AgentManager;
import logic.datamanager.DataManager;
import servlets.utils.ServletUtils;
import servlets.utils.SessionUtils;

import java.io.IOException;
import java.util.Set;

/**
 * This servlet return the users that connected to the main user
 * when contest it returns the teams that participate
 * when team it returns the players(agents) that he have
 *
 * need to fix and make the method of getConnected.. in teamsManager it should reply the agent details dto
 */

@WebServlet(name = "getUsersServlet" , urlPatterns = "/getUsers")
public class GetUsersServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String userRequest = SessionUtils.getUsername(req);
        String userAccess = SessionUtils.getAccess(req);

        try {
            DataManager manager = ServletUtils.getDataManager(req.getServletContext(), userAccess);

            if (userRequest == null || !manager.isUserExists(userRequest)) {
                ServletUtils.createResponse(resp, HttpServletResponse.SC_UNAUTHORIZED, "user not exist");
                //resp.sendRedirect(); -> want to send redirect to login servlet/page.
                return;
            }

            Set<ActivePlayerDTO> users;

            if(manager instanceof AgentManager){
                users = ServletUtils.getTeamsManager(req.getServletContext()).getTeamsDetails();
            }else{
                users = manager.getConnectedUsersDetails(userRequest);
            }

            ServletUtils.createResponse(resp, HttpServletResponse.SC_OK,ServletUtils.GSON_INSTANCE.toJson(users));

        }catch (Exception | Error e){
            ServletUtils.createResponse(resp, HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
        }
    }
}