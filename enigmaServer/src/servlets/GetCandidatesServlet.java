package servlets;

import com.google.gson.Gson;
import decryptionDtos.AgentAnswerDTO;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.datamanager.CandidatesManager;
import servlets.utils.ServletUtils;
import servlets.utils.SessionUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import static servlets.utils.ServletUtils.*;

public class GetCandidatesServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        //if "consumer" == ContestManager....
        //else if "consumer" == TeamManager...

        if(request.getParameterMap().containsKey("usertype")){
            String userType = request.getParameter("usertype");
            switch (userType){
                case CONTEST_MANAGER_ATTRIBUTE_NAME:
                    updateCandidatesInUBoat(request, response);
                    break;
                case TEAM_MANAGER_ATTRIBUTE_NAME:
                    //handle with getting candidates from Agent to Allies
                    break;
            }
        }
        else{
            //error
        }



    }

    public void updateCandidatesInUBoat(HttpServletRequest request, HttpServletResponse response){
        response.setContentType("application/json");
        CandidatesManager candidatesManager = ServletUtils.getCandidatesManager(getServletContext());
        String username = SessionUtils.getUsername(request);
        if (username == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }

        List<AgentAnswerDTO> answersEntries;
        synchronized (getServletContext()) {
            answersEntries = candidatesManager.getCandidates();
        }

        // log and create the response json string

        Gson gson = new Gson();
        String jsonResponse = gson.toJson(answersEntries);

        try (PrintWriter out = response.getWriter()) {
            out.print(jsonResponse);
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


//    private static class ChatAndVersion {
//
//        final private List<SingleChatEntry> entries;
//        final private int version;
//
//        public ChatAndVersion(List<SingleChatEntry> entries, int version) {
//            this.entries = entries;
//            this.version = version;
//        }
//    }
}
