package servlets;

import consoleComponents.OutputMessages;
import logic.*;
import manager.DecryptionManager;
import servlets.utils.SessionUtils;
import servlets.utils.servletUtils;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;

@WebServlet(name = "ReadFileServlet", urlPatterns = "/uBoat/loadFile")
public class ReadFileServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        String userName = SessionUtils.getUsername(req);
        ContestsManager contestsManager = servletUtils.getContestManager(req.getServletContext());

        if(userName == null || !contestsManager.isUserExists(userName)) {
            servletUtils.createResponse(resp,HttpServletResponse.SC_UNAUTHORIZED,null);
            //resp.sendRedirect(); -> want to send redirect to login servlet/page.
            return;
        }

        try {
            String fileContent = servletUtils.getBody(req);
            if(contestsManager.addContestForUser(userName,createContestDetailsForUser(fileContent,resp))) {
                servletUtils.createResponse(resp,HttpServletResponse.SC_OK, OutputMessages.getSuccessfulLoadMsg());
            }
            else{
                servletUtils.createResponse(resp,HttpServletResponse.SC_CONFLICT,null);
            }
        }catch (Exception e){
            servletUtils.createResponse(resp,HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE,e.getMessage());
        }
    }

    private UserContest createContestDetailsForUser(String xmlFileContent, HttpServletResponse resp){

        EnigmaEngine engineForUser = new EnigmaEngine();
        DecryptionManager dmForUser;
        BattleField field;

        try {
            engineForUser.loadXmlFile(new ByteArrayInputStream(xmlFileContent.getBytes()));
            dmForUser = DecipherLogic.createDecryptionMangerFromDecipher(engineForUser);
            field = BattleField.createBattleField(engineForUser.getCTEBattleField());
        } catch (Exception e) {
            servletUtils.createResponse(resp,HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE,e.getMessage());
            return null;
        }

        return new UserContest(engineForUser,dmForUser,field);
    }

}
