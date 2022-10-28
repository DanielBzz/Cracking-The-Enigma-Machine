package servlets;

import contestDtos.CandidateDataDTO;
import exceptions.ContestNotExistException;
import exceptions.ContestNotReadyException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.datamanager.AgentManager;
import logic.datamanager.ContestsManager;
import logic.datamanager.TeamsManager;
import servlets.utils.ServletUtils;
import servlets.utils.SessionUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static servlets.utils.ServletUtils.GSON_INSTANCE;

@WebServlet(name = "isContestOnServlet", urlPatterns = "isContestOn")
public class isContestOnServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {

        String username = SessionUtils.getUsername(request);
        //need to make it generic
        AgentManager manager = ServletUtils.getAgentManager(request.getServletContext());

        if (username == null || !manager.isUserExists(username)) {
            ServletUtils.createResponse(response, HttpServletResponse.SC_UNAUTHORIZED, null);
            //resp.sendRedirect(); -> want to send redirect to login servlet/page.
            return;
        }

        try{

            Boolean responseMessage = manager.getAgent(username).isInContest();

            ServletUtils.createResponse(response, HttpServletResponse.SC_OK, ServletUtils.GSON_INSTANCE.toJson(responseMessage));

        }catch (ContestNotExistException | ContestNotReadyException e){
            ServletUtils.createResponse(response, HttpServletResponse.SC_UNAUTHORIZED,e.getMessage());
            System.out.println(e.getMessage());
        }catch (Exception e){
            ServletUtils.createResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,null);
        }
    }


}
