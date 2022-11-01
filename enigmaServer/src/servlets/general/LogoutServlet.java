package servlets.general;

import exceptions.UserNotExistException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.datamanager.AgentManager;
import logic.datamanager.ContestsManager;
import logic.datamanager.DataManager;
import logic.datamanager.TeamsManager;
import logic.serverdata.Agent;
import logic.serverdata.Team;
import servlets.utils.ServletUtils;
import servlets.utils.SessionUtils;

import java.io.IOException;

@WebServlet(name = "LogoutServlet", urlPatterns = "/logout")
public class LogoutServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String usernameFromSession = SessionUtils.getUsername(request);
        String accessLevel = SessionUtils.getAccess(request);

        try {
            DataManager manager = ServletUtils.getDataManager(request.getServletContext(),accessLevel);
            removeUser(usernameFromSession,manager,request);
            ServletUtils.createResponse(response,HttpServletResponse.SC_OK,null);
        } catch (Exception e) {
            ServletUtils.createResponse(response,HttpServletResponse.SC_INTERNAL_SERVER_ERROR,e.getMessage());
        }
    }

    public void removeUser(String usernameFromSession, DataManager user, HttpServletRequest request) throws UserNotExistException {
        if (usernameFromSession != null) {
            if(user instanceof TeamsManager){
                ContestsManager contest = ServletUtils.getContestManager(request.getServletContext());
                Team teamToRemove = ((TeamsManager) user).getTeam(usernameFromSession);
                contest.removeCompetitorFromContest(teamToRemove.getContestManagerName(),teamToRemove);
            } else if (user instanceof AgentManager) {
                TeamsManager teamsManager = ServletUtils.getTeamsManager(request.getServletContext());
                Agent agent = ((AgentManager) user).getAgent(usernameFromSession);
                teamsManager.removeAgentFromTeam(agent.getTeamName(),agent);
            }

            user.removeUser(usernameFromSession);
            SessionUtils.clearSession(request);
        }

    }
}
