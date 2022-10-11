package servlets;

import exceptions.MachineNotDefinedException;
import exceptions.NoFileLoadedException;
import exceptions.NotInDictionaryException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.ContestsManager;
import servlets.utils.SessionUtils;
import servlets.utils.servletUtils;

import java.io.IOException;

@WebServlet(name = "encryptServlet" , urlPatterns = "/uBoat/encrypt")
public class EncryptMessageServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userName = SessionUtils.getUsername(req);
        ContestsManager manager = servletUtils.getContestManager(req.getServletContext());
        String messageToEncrypt = req.getParameter(servletUtils.MESSAGE_TO_ENCRYPT_PARAMATER);

        // should save some where the encrypt message to send it to allies / for allies to pull it

        if (userName == null || !manager.isUserExists(userName)) {
            servletUtils.createResponse(resp, HttpServletResponse.SC_UNAUTHORIZED, null);
            //resp.sendRedirect(); -> want to send redirect to login servlet/page.
            return;
        }

        try {
            servletUtils.createResponse(resp, HttpServletResponse.SC_OK, manager.encrypt(userName, messageToEncrypt));
        } catch (MachineNotDefinedException | NoFileLoadedException e) {
            servletUtils.createResponse(resp, HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
        } catch (NotInDictionaryException e) {
            servletUtils.createResponse(resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }
}
