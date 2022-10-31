package servlets.teamsManager;

import contestDtos.ActivePlayerDTO;
import exceptions.ContestNotExistException;
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
import java.util.Set;

@WebServlet(name = "getTeamsInSameContestServlet", urlPatterns = "/teamManager/getTeamsInContest")
public class GetTeamsInSameContestServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String username = SessionUtils.getUsername(req);
        TeamsManager teamsManager = ServletUtils.getTeamsManager(req.getServletContext());

        if(teamsManager.isUserExists(username)){
            ServletUtils.createResponse(resp, HttpServletResponse.SC_UNAUTHORIZED, null);
            return;
        }

        if(teamsManager.getContestName(username) == null || teamsManager.getContestName(username).isEmpty()){
            ServletUtils.createResponse(resp, HttpServletResponse.SC_NO_CONTENT, null);
            return;
        }

        try {
            ContestsManager contestsManager = ServletUtils.getContestManager(req.getServletContext());
            Set<ActivePlayerDTO> competitors = contestsManager.getConnectedUsersDetails(teamsManager.getContestName(username));
            ServletUtils.createResponse(resp, HttpServletResponse.SC_OK, ServletUtils.GSON_INSTANCE.toJson(competitors));
        }catch (ContestNotExistException e){
            ServletUtils.createResponse(resp, HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
        }
    }
}
