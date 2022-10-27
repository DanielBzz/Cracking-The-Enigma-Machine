package servlets;

import agent.AgentTask;
import contestDtos.CandidateDataDTO;
import exceptions.ContestNotExistException;
import exceptions.ContestNotReadyException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.datamanager.AgentManager;
import logic.datamanager.ContestsManager;
import logic.datamanager.TeamsManager;
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
            String teamManagerName = request.getParameter("teamManagerName");
            int amountOfTasks = manager.getAgent(username).getAmountOfTasksInASingleTake();

            //getTasks -> need to go to the specific allies(teamManager) and get amountOfTasks agentTask

            List<AgentTask> newTasks = allies.getTasks(teamManagerName,amountOfTasks);

            ServletUtils.createResponse(response, HttpServletResponse.SC_OK, ServletUtils.GSON_INSTANCE.toJson(newTasks));

        }catch (ContestNotExistException | ContestNotReadyException e){
            ServletUtils.createResponse(response, HttpServletResponse.SC_UNAUTHORIZED,e.getMessage());
            System.out.println(e.getMessage());
        }catch (Exception e){
            ServletUtils.createResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,null);
        }
    }
}
