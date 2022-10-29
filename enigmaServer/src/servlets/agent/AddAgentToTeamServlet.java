package servlets.agent;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.datamanager.AgentManager;
import logic.datamanager.TeamsManager;
import servlets.utils.ServletUtils;
import servlets.utils.SessionUtils;

import java.io.IOException;

@WebServlet(name = "AddAgentToTeamServlet", urlPatterns = "/agentManager/addAgentToTeam")
public class AddAgentToTeamServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String userName = SessionUtils.getUsername(request);
        AgentManager agentManager = ServletUtils.getAgentManager(request.getServletContext());

        if (userName == null || !agentManager.isUserExists(userName)) {
            ServletUtils.createResponse(response, HttpServletResponse.SC_UNAUTHORIZED, null);
            //resp.sendRedirect(); -> want to send redirect to login servlet/page.
            return;
        }

        try{
            int amountOfThreads = Integer.parseInt(request.getParameter("amountOfThreads"));
            int amountOfTasksInASingleTake = Integer.parseInt(request.getParameter("amountOfTasksInASingleTake"));
            String alliesName = request.getParameter("teamManager");
            agentManager.getAgent(userName).setBasicData(amountOfThreads, amountOfTasksInASingleTake, alliesName);

            TeamsManager manager = ServletUtils.getTeamsManager(request.getServletContext());
            String teamManagerName = request.getParameter(ServletUtils.TEAM_MANAGER_ATTRIBUTE_NAME);
            manager.addAgent(teamManagerName);
            ServletUtils.createResponse(response, HttpServletResponse.SC_OK, null);
        }catch(Exception e){
            ServletUtils.createResponse(response, HttpServletResponse.SC_CONFLICT, e.getMessage());
        }

    }
}
