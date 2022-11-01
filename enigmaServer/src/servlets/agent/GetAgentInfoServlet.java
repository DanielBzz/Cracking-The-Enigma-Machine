package servlets.agent;

import contestDtos.AgentInfoDTO;
import exceptions.ContestNotExistException;
import exceptions.ContestNotReadyException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.datamanager.AgentManager;
import servlets.utils.ServletUtils;
import servlets.utils.SessionUtils;

@WebServlet(name = "GetAgentInfoServlet", urlPatterns = "/agentManager/getAgentInfo")
public class GetAgentInfoServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {

        String username = SessionUtils.getUsername(request);
        AgentManager manager = ServletUtils.getAgentManager(request.getServletContext());

        if (username == null || !manager.isUserExists(username)) {
            ServletUtils.createResponse(response, HttpServletResponse.SC_UNAUTHORIZED, null);
            //resp.sendRedirect(); -> want to send redirect to login servlet/page.
            return;
        }
        System.out.println("in");
        try{
            AgentInfoDTO responseMessage = manager.getAgent(username).getAgentInfo();
            String res = ServletUtils.GSON_INSTANCE.toJson(responseMessage);
            System.out.println(res);
            ServletUtils.createResponse(response, HttpServletResponse.SC_OK, res);
        }catch (ContestNotExistException | ContestNotReadyException e){
            ServletUtils.createResponse(response, HttpServletResponse.SC_UNAUTHORIZED,e.getMessage());
            System.out.println(e.getMessage());
        }catch (Exception e){
            ServletUtils.createResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,e.getMessage());
        }
    }
}
