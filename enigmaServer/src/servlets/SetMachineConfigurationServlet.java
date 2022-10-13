package servlets;

import com.google.gson.Gson;
import exceptions.NoFileLoadedException;
import logic.datamanager.ContestsManager;
import machineDtos.EnigmaMachineDTO;
import servlets.utils.SessionUtils;
import servlets.utils.ServletUtils;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "MachineConfigurationServlet", urlPatterns = "/contestManager/initMachine")
public class SetMachineConfigurationServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String userName = SessionUtils.getUsername(req);
        ContestsManager manager = ServletUtils.getContestManager(req.getServletContext());

        if(userName == null || !manager.isUserExists(userName)) {
            ServletUtils.createResponse(resp,HttpServletResponse.SC_UNAUTHORIZED,null);
            //resp.sendRedirect(); -> want to send redirect to login servlet/page.
            return;
        }

        try {
            Gson json = new Gson();
            EnigmaMachineDTO args = json.fromJson(ServletUtils.getBody(req), EnigmaMachineDTO.class);

            manager.initialMachineForUser(userName,args);
            ServletUtils.createResponse(resp,HttpServletResponse.SC_OK,null);

        }catch (NoFileLoadedException e){
            ServletUtils.createResponse(resp,HttpServletResponse.SC_UNAUTHORIZED,e.getMessage());
        }
        catch (Exception | Error e){
            ServletUtils.createResponse(resp,HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE,e.getMessage());
        }
    }
}
