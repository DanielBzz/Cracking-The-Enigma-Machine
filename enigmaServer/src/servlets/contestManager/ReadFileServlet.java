package servlets.contestManager;

import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.DecipherLogic;
import logic.EnigmaEngine;
import logic.datamanager.ContestsManager;
import logic.serverdata.BattleField;
import logic.serverdata.UserContest;
import manager.DecryptionManager;
import servlets.utils.ServletUtils;
import servlets.utils.SessionUtils;

import java.io.ByteArrayInputStream;

@WebServlet(name = "ReadFileServlet", urlPatterns = "/contestManager/loadFile")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
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
            String fileContent = ServletUtils.getBody(req.getParts());
            UserContest contest = createContestDetailsForUser(fileContent,resp, userName);
            if(contest != null && contestsManager.addContestForUser(userName,contest)) {
                String[] jsonStrings = new String[2];
                jsonStrings[0] = ServletUtils.GSON_INSTANCE.toJson(contest.getEngineInfo());
                jsonStrings[1] = ServletUtils.GSON_INSTANCE.toJson(contest.getDictionaryInfo());
                ServletUtils.createResponse(resp,HttpServletResponse.SC_OK,ServletUtils.GSON_INSTANCE.toJson(jsonStrings));
            }
            else{
                ServletUtils.createResponse(resp,HttpServletResponse.SC_CONFLICT,null);
            }
        }catch (Exception e){
            ServletUtils.createResponse(resp,HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE,e.getMessage());
            System.out.println(e.getMessage());
            e.printStackTrace();

        }
    }

    private UserContest createContestDetailsForUser(String xmlFileContent, HttpServletResponse resp, String userName){

        System.out.println(xmlFileContent);
        EnigmaEngine engineForUser = new EnigmaEngine();
        DecryptionManager dmForUser;
        BattleField field;

        try {
            engineForUser.loadXmlFile(new ByteArrayInputStream(xmlFileContent.getBytes()));
            dmForUser = DecipherLogic.createDecryptionMangerFromDecipher(engineForUser);
            field = BattleField.createBattleField(engineForUser.getCTEBattleField());
        } catch (Exception e) {
            ServletUtils.createResponse(resp,HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE,e.getMessage());
            System.out.println(e.getMessage());
            e.printStackTrace();
            return null;
        }

        return new UserContest(engineForUser,dmForUser,field, userName);
    }
}
