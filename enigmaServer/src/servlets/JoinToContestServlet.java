package servlets;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import logic.datamanager.ContestsManager;
import logic.datamanager.TeamsManager;
import servlets.utils.ServletUtils;
import servlets.utils.SessionUtils;

import java.io.IOException;

@WebServlet(name = "JoinToContestServlet", urlPatterns = "/teamManager/joinToContestServlet")
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
        String contestName = request.getParameter(ServletUtils.CONTEST_NAME_PARAMETER);

        if(manager.addCompetitorToContest(contestName,teamsManager.getTeam(userName))){
            ServletUtils.createResponse(response, HttpServletResponse.SC_OK, null);
        }
        else {
            ServletUtils.createResponse(response, HttpServletResponse.SC_CONFLICT, null);
            // need to explain why in response, maybe you already in the contest/for the uBoat still not load contest ....
            // need to check also that the allie client not in other contest.
        }
    }

}
