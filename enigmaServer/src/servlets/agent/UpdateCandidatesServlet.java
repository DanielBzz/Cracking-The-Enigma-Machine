package servlets.agent;

import contestDtos.CandidateDataDTO;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.datamanager.AgentManager;
import logic.datamanager.TeamsManager;
import servlets.utils.ServletUtils;
import servlets.utils.SessionUtils;

import java.util.Arrays;
import java.util.stream.Collectors;

import static servlets.utils.ServletUtils.GSON_INSTANCE;
import static servlets.utils.ServletUtils.getAgentManager;

@WebServlet(name = "UpdateCandidatesServlet", urlPatterns = "/agentManager/addCandidates")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class UpdateCandidatesServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response){
        String userName = SessionUtils.getUsername(request);
        AgentManager agentManager = getAgentManager(request.getServletContext());

        if(userName == null || !agentManager.isUserExists(userName) || agentManager.getTeamName(userName) == null){
            System.out.println("-------------------------------------------in update candidates, problem with the user name / team does not exists");
            ServletUtils.createResponse(response, HttpServletResponse.SC_UNAUTHORIZED, null);
            return;
        }

        String teamName = agentManager.getTeamName(userName);
        TeamsManager teamsManager = ServletUtils.getTeamsManager(request.getServletContext());

        try{
            System.out.println("-------------------------------------------in the try of update candidate-------------------------------------");
            CandidateDataDTO[] newCandidates = GSON_INSTANCE.fromJson(ServletUtils.getBody(request.getParts()), CandidateDataDTO[].class);
            System.out.println("-------------------------------------------going to add the new candidates------------------------------------");
            teamsManager.addCandidates(teamName,Arrays.stream(newCandidates).collect(Collectors.toList()));
            ServletUtils.createResponse(response, HttpServletResponse.SC_OK, null);
        } catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
            System.out.println("There is not a body for this request");
            ServletUtils.createResponse(response, HttpServletResponse.SC_CONFLICT, null);
        }
    }
}
