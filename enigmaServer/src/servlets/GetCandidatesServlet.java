package servlets;

import contestDtos.CandidateDataDTO;
import exceptions.ContestNotExistException;
import exceptions.ContestNotReadyException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.datamanager.ContestsManager;
import servlets.utils.ServletUtils;
import servlets.utils.SessionUtils;

import java.util.List;

@WebServlet(name = "getCandidatesServlet", urlPatterns = "/getCandidates")
public class GetCandidatesServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {

        String username = SessionUtils.getUsername(request);
        ContestsManager manager = ServletUtils.getContestManager(request.getServletContext());

        if (username == null || !manager.isUserExists(username)) {
            ServletUtils.createResponse(response, HttpServletResponse.SC_UNAUTHORIZED, null);
            //resp.sendRedirect(); -> want to send redirect to login servlet/page.
            return;
        }

        try{
            int userVersion = Integer.parseInt(request.getParameter("version"));
            List<CandidateDataDTO> newCandidates = manager.getCandidates(username,userVersion);
            int newVersion = userVersion + newCandidates.size();
            String[] responseMessage = new String[2];
            responseMessage[0] = String.valueOf(newVersion);
            responseMessage[1] = ServletUtils.GSON_INSTANCE.toJson(newCandidates);

            ServletUtils.createResponse(response, HttpServletResponse.SC_OK, ServletUtils.GSON_INSTANCE.toJson(responseMessage));

        }catch (ContestNotExistException | ContestNotReadyException e){
            ServletUtils.createResponse(response, HttpServletResponse.SC_UNAUTHORIZED,e.getMessage());
            System.out.println(e.getMessage());
        }catch (Exception e){
            ServletUtils.createResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,null);
        }
    }
}