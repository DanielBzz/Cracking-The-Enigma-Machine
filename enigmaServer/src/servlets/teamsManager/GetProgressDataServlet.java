package servlets.teamsManager;

import contestDtos.ActivePlayerDTO;
import contestDtos.AlliesProgressDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.datamanager.AgentManager;
import logic.datamanager.DataManager;
import logic.datamanager.TeamsManager;
import servlets.utils.ServletUtils;
import servlets.utils.SessionUtils;

import java.io.IOException;
import java.util.Set;

@WebServlet(name = "getProgressDataServlet" , urlPatterns = "/teamManager/getProgressData")
public class GetProgressDataServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String userName = SessionUtils.getUsername(req);

        try {
            TeamsManager manager = ServletUtils.getTeamsManager(req.getServletContext());

            if (userName == null || !manager.isUserExists(userName)) {
                ServletUtils.createResponse(resp, HttpServletResponse.SC_UNAUTHORIZED, "user not exist");
                //resp.sendRedirect(); -> want to send redirect to login servlet/page.
                return;
            }

            int producedTasks = manager.getTeamsProducedTasks(userName);
            int totalFinishedTasks = manager.getTeamsTotalFinishedTasks(userName);

            ServletUtils.createResponse(resp, HttpServletResponse.SC_OK,ServletUtils.GSON_INSTANCE.toJson(new AlliesProgressDTO(producedTasks, totalFinishedTasks)));

        }catch (Exception | Error e){
            ServletUtils.createResponse(resp, HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
        }
    }
}
