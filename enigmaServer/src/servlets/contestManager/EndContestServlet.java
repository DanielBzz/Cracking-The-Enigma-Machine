package servlets.contestManager;

import contestDtos.CandidateDataDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.datamanager.ContestsManager;
import servlets.utils.ServletUtils;
import servlets.utils.SessionUtils;

import java.io.IOException;

@WebServlet(name = "endContestServlet", urlPatterns = "/contestManager/endContest")
public class EndContestServlet extends HttpServlet {

    private static final String WINNER_PARAMETER = "winnerTeam";
    private static final String CONFIGURATION_PARAMETER = "configuration";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String username = SessionUtils.getUsername(req);
        ContestsManager contestsManager = ServletUtils.getContestManager(req.getServletContext());

        if(username==null || !contestsManager.isUserExists(username)){
            ServletUtils.createResponse(resp,resp.SC_UNAUTHORIZED,null);
            return;
        }

        try {
            String decryptedMessage = req.getParameter(ServletUtils.MESSAGE_PARAMETER);
            String winnerTeam = req.getParameter(WINNER_PARAMETER);
            String configuration = req.getParameter(CONFIGURATION_PARAMETER);
            CandidateDataDTO winnerCandidate = new CandidateDataDTO(decryptedMessage,winnerTeam,configuration);
            contestsManager.endUserContest(username, winnerCandidate);
        }catch (Exception e){
            ServletUtils.createResponse(resp,resp.SC_CONFLICT,e.getMessage());

        }

    }

}
