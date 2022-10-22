package servlets.contestManager;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.datamanager.ContestsManager;
import servlets.utils.ServletUtils;
import servlets.utils.SessionUtils;

import java.io.IOException;

@WebServlet(name = "UBoatReadyServlet", urlPatterns = "/contestManager/setReady")
public class UBoatReadyServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String userName = SessionUtils.getUsername(request);
        ContestsManager manager = ServletUtils.getContestManager(request.getServletContext());

        if (userName == null || !manager.isUserExists(userName)) {
            ServletUtils.createResponse(response, HttpServletResponse.SC_UNAUTHORIZED, null);
            //resp.sendRedirect(); -> want to send redirect to login servlet/page.
            return;
        }

        try{
            String encryptedMessage = request.getParameter("encryptedMessage");
            manager.setContestUserReady(userName, encryptedMessage);
            manager.checkIfNeedToStartContest(userName);
            ServletUtils.createResponse(response, HttpServletResponse.SC_OK, null);

        } catch (Exception e){
            ServletUtils.createResponse(response, HttpServletResponse.SC_CONFLICT, e.getMessage());
            System.out.println(e.getMessage());
        }
    }
}
