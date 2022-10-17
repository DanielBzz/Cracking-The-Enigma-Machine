package servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import contestDtos.CandidateDataDTO;
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
import java.util.List;

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
            //need a general module for deserialize things
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(CandidateDataDTO.class, new CandidateDeserializer)
                    .create();
            //need to read list and not a single candidate
            List<CandidateDataDTO> newCandidates = gson.fromJson(request.getReader(), CandidateDataDTO.class);

            if(teamsManager.isUserExists(teamManagerName)){
                //ContestsManager contestsManager = ServletUtils.getContestManager(teamsManager.getUBoatName(teamManagerName));
                teamsManager.getTeam(teamManagerName).addCandidates(newCandidates);
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