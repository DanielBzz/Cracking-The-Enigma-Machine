package servlets;

import contestDtos.CandidateDataDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.datamanager.TeamsManager;
import servlets.utils.ServletUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

import static servlets.utils.ServletUtils.GSON_INSTANCE;

@WebServlet(name = "UpdateCandidatesServlet", urlPatterns = "/agent/addCandidates")
public class UpdateCandidatesServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //String userName = SessionUtils.getUsername(request);

        String teamManagerName = request.getParameter("teamManager");//maybe insert to try and catch
        TeamsManager teamsManager = ServletUtils.getTeamsManager(request.getServletContext());

        if (teamManagerName == null) {
            ServletUtils.createResponse(response, HttpServletResponse.SC_UNAUTHORIZED, null);
            //resp.sendRedirect(); -> want to send redirect to login servlet/page.
            return;
        }

        try{

            CandidateDataDTO newCandidates[] = GSON_INSTANCE.fromJson(request.getReader(), CandidateDataDTO[].class);

            if(teamsManager.isUserExists(teamManagerName)){
                teamsManager.getTeam(teamManagerName).addCandidates(Arrays.stream(newCandidates).collect(Collectors.toList()));
                ServletUtils.createResponse(response, HttpServletResponse.SC_OK, null);
            }
            else {
                ServletUtils.createResponse(response, HttpServletResponse.SC_CONFLICT, null);
                // need to explain why in response, maybe you already in the contest/for the uBoat still not load contest ....
                // need to check also that the allie client not in other contest.
            }
        } catch (Exception e){
            System.out.println("There is not a body for this request");
        }

    }
}
//------------------agent need to add the teams name in query parameter