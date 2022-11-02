package servlets.general;

import contestDtos.ContestDetailsDTO;
import exceptions.ContestNotExistException;
import exceptions.UserNotExistException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.datamanager.AgentManager;
import logic.datamanager.ContestsManager;
import logic.datamanager.DataManager;
import logic.datamanager.TeamsManager;
import servlets.utils.ServletUtils;
import servlets.utils.SessionUtils;

@WebServlet(name = "isContestOnServlet", urlPatterns = "/isContestOn")
public class isContestOnServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {

        String username = SessionUtils.getUsername(request);
        String contestManagerName = null;
        DataManager manager = null;

        try{
            manager = ServletUtils.getDataManager(request.getServletContext(),SessionUtils.getAccess(request));

            if (username == null || !manager.isUserExists(username)) {
                ServletUtils.createResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "user not exist");
                //resp.sendRedirect(); -> want to send redirect to login servlet/page.
                return;
            }

            if(manager instanceof AgentManager){
                String teamOfAgent = ((AgentManager) manager).getTeamName(username);
                TeamsManager teamManager = ServletUtils.getTeamsManager(request.getServletContext());
                contestManagerName = teamManager.getContestName(teamOfAgent);
            } else if (manager instanceof TeamsManager) {
                contestManagerName = ((TeamsManager) manager).getContestName(username);
            }

            ContestsManager contestsManager = ServletUtils.getContestManager(request.getServletContext());

            ContestDetailsDTO contestDetails = contestsManager.getContestDetails(contestManagerName);
            ServletUtils.createResponse(response, HttpServletResponse.SC_OK, ServletUtils.GSON_INSTANCE.toJson(contestDetails));

        }catch (UserNotExistException e){
            ServletUtils.createResponse(response, HttpServletResponse.SC_RESET_CONTENT, e.getMessage());
            System.out.println(e.getMessage());
        } catch(ContestNotExistException e){
            if(manager instanceof AgentManager){
                ContestDetailsDTO contestDetails = new ContestDetailsDTO(null,null,false,null,null,0,null);
                ServletUtils.createResponse(response, HttpServletResponse.SC_OK,ServletUtils.GSON_INSTANCE.toJson(contestDetails));
            }else {
                ServletUtils.createResponse(response, HttpServletResponse.SC_NO_CONTENT, "still not connect to contest");
            }
        }catch (Exception | Error e){
            ServletUtils.createResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,e.getMessage());
        }
    }


}
