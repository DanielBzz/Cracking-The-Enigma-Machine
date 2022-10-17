package servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import contestDtos.ActivePlayerDTO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import logic.datamanager.ContestsManager;
import logic.datamanager.TeamsManager;
import logic.serverdata.Team;
import servlets.utils.ServletUtils;
import servlets.utils.SessionUtils;
import servlets.utils.TeamSerealizer;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
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

        if(manager.addCompetitorToContest(contestManagerName,teamsManager.getTeam(userName))){
            teamsManager.getTeam(userName).setuBoatName(contestManagerName);
            response.getWriter().write(getCompetitorsAsString(manager, contestManagerName));
            ServletUtils.createResponse(response, HttpServletResponse.SC_OK, null);
        }
        else {
            ServletUtils.createResponse(response, HttpServletResponse.SC_CONFLICT, null);
            // need to explain why in response, maybe you already in the contest/for the uBoat still not load contest ....
            // need to check also that the allie client not in other contest.
        }
    }
    protected String getCompetitorsAsString(ContestsManager manager, String contestManagerName){
        Set<Team> competitors = new HashSet<>(manager.getCompetitors(contestManagerName));
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Team.class, new TeamSerealizer())
                .create();
        String competitorsAsString = "";

        for (Team t:competitors) {
            competitorsAsString = competitorsAsString + gson.toJson(t);
        }
        return competitorsAsString;
    }

}
