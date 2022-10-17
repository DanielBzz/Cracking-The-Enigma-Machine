package servlets;

import exceptions.MachineNotDefinedException;
import exceptions.NoFileLoadedException;
import exceptions.NotInDictionaryException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.datamanager.ContestsManager;
import machineDtos.EngineWithEncryptDTO;
import servlets.utils.SessionUtils;
import servlets.utils.ServletUtils;

import java.io.IOException;

@WebServlet(name = "encryptServlet" , urlPatterns = "/contestManager/encrypt")
public class EncryptMessageServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userName = SessionUtils.getUsername(req);
        ContestsManager manager = ServletUtils.getContestManager(req.getServletContext());
        String messageToEncrypt = req.getParameter(ServletUtils.MESSAGE_TO_ENCRYPT_PARAMETER);

        // should save some where the encrypt message to send it to allies / for allies to pull it

        if (userName == null || !manager.isUserExists(userName)) {
            ServletUtils.createResponse(resp, HttpServletResponse.SC_UNAUTHORIZED, null);
            //resp.sendRedirect(); -> want to send redirect to login servlet/page.
            return;
        }

        try {
            String responseMsg = manager.encrypt(userName, messageToEncrypt);
            EngineWithEncryptDTO responseBody = new EngineWithEncryptDTO(manager.getUserEngineDetails(userName),responseMsg);
            ServletUtils.createResponse(resp, HttpServletResponse.SC_OK, ServletUtils.GSON_INSTANCE.toJson(responseBody));
        } catch (MachineNotDefinedException | NoFileLoadedException e) {
            ServletUtils.createResponse(resp, HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
        } catch (NotInDictionaryException e) {
            ServletUtils.createResponse(resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }catch (Exception | Error e){
            ServletUtils.createResponse(resp,HttpServletResponse.SC_INTERNAL_SERVER_ERROR,e.getMessage());
        }
    }
}
