package servlets.agent;

import contestDtos.CandidateDataDTO;
import contestDtos.ContestDetailsDTO;
import exceptions.UserNotExistException;
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

@WebServlet(name = "isContestOnServlet", urlPatterns = "/agentManager/isContestOn")
public class isContestOnServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {

        String username = SessionUtils.getUsername(request);
        //need to make it generic
        AgentManager agentManager = ServletUtils.getAgentManager(request.getServletContext());

        if (username == null || !agentManager.isUserExists(username)) {
            ServletUtils.createResponse(response, HttpServletResponse.SC_UNAUTHORIZED, null);
            //resp.sendRedirect(); -> want to send redirect to login servlet/page.
            return;
        }

        try{
            Boolean inContest = agentManager.getAgent(username).isInContest();
            String[] responseMessage = new String[2];
            responseMessage[0] = String.valueOf(inContest);
            responseMessage[1] = null;
            if(inContest){
                String teamsManagerName = agentManager.getTeamName(username);
                TeamsManager teamManager = ServletUtils.getTeamsManager(request.getServletContext());

                String contestManagerName = teamManager.getContestName(teamsManagerName);
                ContestsManager contestsManager = ServletUtils.getContestManager(request.getServletContext());

                ContestDetailsDTO contestDetails = contestsManager.getContestDetails(contestManagerName);
                responseMessage[1] = ServletUtils.GSON_INSTANCE.toJson(contestDetails);
            }


            ServletUtils.createResponse(response, HttpServletResponse.SC_OK, ServletUtils.GSON_INSTANCE.toJson(responseMessage));
        }catch (UserNotExistException e){
            ServletUtils.createResponse(response, HttpServletResponse.SC_UNAUTHORIZED,e.getMessage());
            System.out.println(e.getMessage());
        }catch (Exception e){
            ServletUtils.createResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,null);
        }
    }


}
