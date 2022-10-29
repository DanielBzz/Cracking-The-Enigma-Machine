package servlets.general;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.datamanager.ContestsManager;
import logic.datamanager.DataManager;
import servlets.utils.ServletUtils;
import servlets.utils.SessionUtils;

import java.io.IOException;

import static servlets.utils.ServletUtils.*;

@WebServlet(name = "LogoutServlet", urlPatterns = {"/logout"})
public class LogoutServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String usernameFromSession = SessionUtils.getUsername(request);
        String userType = request.getParameter("usertype");
        switch (userType){
            case CONTEST_MANAGER_ATTRIBUTE_NAME:
                ContestsManager contestManager = ServletUtils.getContestManager(getServletContext());
                removeUser(usernameFromSession, contestManager, request);
                break;
            case TEAM_MANAGER_ATTRIBUTE_NAME:

                //handle with allies logout
                break;
            case AGENT_ATTRIBUTE_NAME:
                //handle with agent logout
                break;
            default:
                //error
                break;
        }
        SessionUtils.clearSession(request);
    }
    public void removeUser(String usernameFromSession, DataManager user, HttpServletRequest request){
        if (usernameFromSession != null) {
            user.removeUser(usernameFromSession);
            SessionUtils.clearSession(request);
        }
    }
}
