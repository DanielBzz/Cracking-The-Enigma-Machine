package servlets.teamsManager;

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

@WebServlet(name = "TeamIsReadyServlet", urlPatterns = "/teamManager/setTeamReady")
public class TeamIsReadyServlet extends HttpServlet {

    private static final String TASK_SIZE_ATTRIBUTE = "taskSize";

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

        try {
            int teamTaskSize = Integer.parseInt(request.getParameter(TASK_SIZE_ATTRIBUTE));
            teamsManager.setUserReady(userName,teamTaskSize);
            manager.checkIfNeedToStartContest(teamsManager.getContestName(userName));
            ServletUtils.createResponse(response, HttpServletResponse.SC_OK, null);
        } catch (Exception e) {
            ServletUtils.createResponse(response, HttpServletResponse.SC_CONFLICT, e.getMessage());
        }
    }
}
