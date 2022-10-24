package servlets;

import contestDtos.ActivePlayerDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.datamanager.DataManager;
import servlets.utils.ServletUtils;
import servlets.utils.SessionUtils;

import java.io.IOException;
import java.util.Set;

@WebServlet(name = "getUsersServlet" , urlPatterns = "/getUsers")
public class GetUsersServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String userRequest = SessionUtils.getUsername(req);
        String userAccess = SessionUtils.getAccess(req);

        try {
            DataManager manager = ServletUtils.getDataManager(req.getServletContext(), userAccess);

            if (userRequest == null || !manager.isUserExists(userRequest)) {
                ServletUtils.createResponse(resp, HttpServletResponse.SC_UNAUTHORIZED, null);
                //resp.sendRedirect(); -> want to send redirect to login servlet/page.
                return;
            }

            Set<ActivePlayerDTO> users = manager.getConnectedUsersDetails(userRequest);
            ServletUtils.createResponse(resp, HttpServletResponse.SC_OK,ServletUtils.GSON_INSTANCE.toJson(users));

        }catch (Exception | Error e){
            ServletUtils.createResponse(resp, HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
        }
    }
}