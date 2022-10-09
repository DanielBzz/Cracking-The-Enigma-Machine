package servlets;

import consoleComponents.OutputMessages;
import logic.*;
import manager.DecryptionManager;
import servlets.utils.SessionUtils;
import servlets.utils.servletUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Collection;

@WebServlet(name = "ReadFileServlet", urlPatterns = "/uBoat/loadFile")
public class ReadFileServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userName = SessionUtils.getUsername(req);
        ContestsManager contestsManager = servletUtils.getContestManager(req.getServletContext());

        if(userName == null || !contestsManager.isUserExists(userName)) {
            servletUtils.createResponse(resp,HttpServletResponse.SC_UNAUTHORIZED,null);
            //resp.sendRedirect(); -> want to send redirect to login servlet/page.
        }

        Collection<Part> fileParts = req.getParts();
        StringBuilder fileContent = new StringBuilder();
        fileParts.forEach(part -> fileContent.append(part.getContentType()));

        if(contestsManager.addContestForUser(userName,createContestDetailsForUser(fileContent.toString(),resp))) {
            servletUtils.createResponse(resp,HttpServletResponse.SC_OK, OutputMessages.getSuccessfulLoadMsg());
        }
        else{
            servletUtils.createResponse(resp,HttpServletResponse.SC_CONFLICT,"for the user: " + userName + " there is already file loaded.");
        }
    }

    private ContestDetails createContestDetailsForUser(String xmlFileContent,HttpServletResponse resp){

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

        return new ContestDetails(engineForUser,dmForUser,field);
    }

}
