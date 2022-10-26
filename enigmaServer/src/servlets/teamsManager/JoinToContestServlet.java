package servlets.teamsManager;

import contestDtos.ActivePlayerDTO;
import contestDtos.ContestDetailsDTO;
import contestDtos.TeamDetailsContestDTO;
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

@WebServlet(name = "JoinToContestServlet", urlPatterns = "/teamManager/joinToContest")
public class JoinToContestServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String userName = SessionUtils.getUsername(request);
        TeamsManager teamsManager = ServletUtils.getTeamsManager(request.getServletContext());

        if (userName == null || !teamsManager.isUserExists(userName)) {
            ServletUtils.createResponse(response, HttpServletResponse.SC_UNAUTHORIZED, null);
            //resp.sendRedirect(); -> want to send redirect to login servlet/page.
            return;
        }

        ContestsManager manager = ServletUtils.getContestManager(request.getServletContext());
        String contestManagerName = request.getParameter(ServletUtils.CONTEST_MANAGER_ATTRIBUTE_NAME);
        try {
            manager.addCompetitorToContest(contestManagerName,teamsManager.getTeam(userName));
            ContestDetailsDTO contestDetails = manager.getContestDetails(contestManagerName);
            Set<ActivePlayerDTO> competitorsTeams = manager.getConnectedUsersDetails(contestManagerName);
            Set<ActivePlayerDTO> teamAgents = teamsManager.getConnectedUsersDetails(userName);

            TeamDetailsContestDTO responseDetails = new TeamDetailsContestDTO(contestDetails,competitorsTeams,teamAgents);
            ServletUtils.createResponse(response, HttpServletResponse.SC_OK, ServletUtils.GSON_INSTANCE.toJson(responseDetails));
        }catch (ContestNotExistException e){
            ServletUtils.createResponse(response, HttpServletResponse.SC_CONFLICT, e.getMessage());
        }
    }
}
