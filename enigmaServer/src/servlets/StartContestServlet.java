package servlets;

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
import java.util.Enumeration;
//-----------------------------not ready yet!!!!!--------------------------
@WebServlet(name = "StartContestServlet", urlPatterns = "/contestManager/startContest")
public class StartContestServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String userName = SessionUtils.getUsername(request);
        TeamsManager teamsManager = ServletUtils.getTeamsManager(request.getServletContext());

        if (userName == null || !teamsManager.isUserExists(userName)) {
            ServletUtils.createResponse(response, HttpServletResponse.SC_UNAUTHORIZED, null);
            //resp.sendRedirect(); -> want to send redirect to login servlet/page.
            return;
        }

        ContestsManager manager = ServletUtils.getContestManager(request.getServletContext());
        String contestManagerName = request.getParameter(ServletUtils.CONTEST_MANAGER_ATTRIBUTE_NAME);

        if(manager.updateUserDetails(contestManagerName,teamsManager.getTeam(userName))){
            ServletUtils.createResponse(response, HttpServletResponse.SC_OK, null);
        }
        else {
            ServletUtils.createResponse(response, HttpServletResponse.SC_CONFLICT, null);
            // need to explain why in response, maybe you already in the contest/for the uBoat still not load contest ....
            // need to check also that the allie client not in other contest.
        }

    }
    public void updateAllAlliesToStartTheContest(HttpServletRequest req){
        Enumeration<String> attributes = req.getSession().getAttributeNames();
        while (attributes.hasMoreElements()) {
            String attribute = (String) attributes.nextElement();
            //need to find the right url of this ally
            try{
                //doGet(req, resp);
                //need to send a message to the curr ally to start working (like pressing the START button)
            }catch (Exception e){

            }
        }
    }
}
