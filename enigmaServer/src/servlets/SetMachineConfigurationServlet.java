package servlets;

import com.google.gson.Gson;
import exceptions.NoFileLoadedException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.datamanager.ContestsManager;
import machineDtos.MachineInfoDTO;
import servlets.utils.ServletUtils;
import servlets.utils.SessionUtils;

import java.io.IOException;

@WebServlet(name = "MachineConfigurationServlet", urlPatterns = "/contestManager/initMachine")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
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
            MachineInfoDTO args = json.fromJson(ServletUtils.getBody(req.getParts()), MachineInfoDTO.class);

            manager.initialMachineForUser(userName,args);

            ServletUtils.createResponse(resp,HttpServletResponse.SC_OK,ServletUtils.GSON_INSTANCE.toJson(manager.getUserEngineDetails(userName)));

        }catch (NoFileLoadedException e){
            ServletUtils.createResponse(resp,HttpServletResponse.SC_UNAUTHORIZED,e.getMessage());
        }
        catch (Exception | Error e){
            ServletUtils.createResponse(resp,HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE,e.getMessage());
        }
    }
}
