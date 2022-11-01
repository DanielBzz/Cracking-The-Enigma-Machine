package servlets.agent;

import agent.AgentTask;
import exceptions.ContestNotReadyException;
import exceptions.UserNotExistException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.datamanager.AgentManager;
import logic.datamanager.TeamsManager;
import logic.serverdata.Agent;
import servlets.utils.ServletUtils;
import servlets.utils.SessionUtils;

import java.util.List;

@WebServlet(name = "getTasksServlet", urlPatterns = "/agentManager/getTasks")
public class GetTasksServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {

        String username = SessionUtils.getUsername(request);
        AgentManager manager = ServletUtils.getAgentManager(request.getServletContext());

        if (username == null || !manager.isUserExists(username)) {
            ServletUtils.createResponse(response, HttpServletResponse.SC_UNAUTHORIZED, null);
            //resp.sendRedirect(); -> want to send redirect to login servlet/page.
            return;
        }

        try{
            TeamsManager allies = ServletUtils.getTeamsManager(request.getServletContext());
            Agent agentRequest = manager.getAgent(username);
            List<AgentTask> newTasks = allies.getTasks(agentRequest.getTeamName(),agentRequest.getAmountOfTasksInASingleTake());

            ServletUtils.createResponse(response, HttpServletResponse.SC_OK, ServletUtils.GSON_INSTANCE.toJson(newTasks));
        }catch (UserNotExistException | ContestNotReadyException e){
            ServletUtils.createResponse(response, HttpServletResponse.SC_CONFLICT,e.getMessage());
            System.out.println(e.getMessage());
        }catch (Exception e){
            ServletUtils.createResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,null);
        }
    }
}
