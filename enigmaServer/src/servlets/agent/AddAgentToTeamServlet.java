package servlets.agent;


import contestDtos.ActivePlayerDTO;
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

        System.out.println("in add agent to team servlet");
        String userName = SessionUtils.getUsername(request);
        AgentManager agentManager = ServletUtils.getAgentManager(request.getServletContext());

        System.out.println("is " + userName + "exists: " + agentManager.isUserExists(userName));
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
            ActivePlayerDTO newAgent = new ActivePlayerDTO(userName, amountOfThreads, amountOfTasksInASingleTake);
            System.out.println("in AddAgentToTeamServlet, going to add: " + newAgent);

            manager.addAgent(teamManagerName, newAgent);

            ServletUtils.createResponse(response, HttpServletResponse.SC_OK, null);
        }catch(Exception e){
            System.out.println("in AddAgentToTeamServlet, had an exception: " + e.getMessage());
            ServletUtils.createResponse(response, HttpServletResponse.SC_CONFLICT, e.getMessage());
        }

    }
}
