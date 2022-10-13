package servlets;

import consoleComponents.OutputMessages;
import logic.*;
import logic.datamanager.ContestsManager;
import logic.serverdata.BattleField;
import logic.serverdata.UserContest;
import manager.DecryptionManager;
import servlets.utils.SessionUtils;
import servlets.utils.ServletUtils;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;

@WebServlet(name = "ReadFileServlet", urlPatterns = "/contestManager/loadFile")
public class ReadFileServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        String userName = SessionUtils.getUsername(req);
        ContestsManager contestsManager = ServletUtils.getContestManager(req.getServletContext());

        if(userName == null || !contestsManager.isUserExists(userName)) {
            ServletUtils.createResponse(resp,HttpServletResponse.SC_UNAUTHORIZED,null);
            //resp.sendRedirect(); -> want to send redirect to login servlet/page.
            return;
        }

        try {
            String fileContent = ServletUtils.getBody(req);
            if(contestsManager.addContestForUser(userName,createContestDetailsForUser(fileContent,resp))) {
                ServletUtils.createResponse(resp,HttpServletResponse.SC_OK, OutputMessages.getSuccessfulLoadMsg());
            }
            else{
                ServletUtils.createResponse(resp,HttpServletResponse.SC_CONFLICT,null);
            }
        }catch (Exception e){
            ServletUtils.createResponse(resp,HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE,e.getMessage());
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
            ServletUtils.createResponse(resp,HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE,e.getMessage());
            return null;
        }

        return new UserContest(engineForUser,dmForUser,field);
    }

}
