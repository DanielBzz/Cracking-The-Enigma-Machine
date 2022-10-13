package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import servlets.utils.ServletUtils;

import java.io.IOException;
import java.util.Enumeration;

@WebServlet(name = "MachineConfigurationServlet", urlPatterns = "/contestManager/startContest")
public class StartContestServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        ServletUtils.createResponse(resp,HttpServletResponse.SC_OK,null);

    }
    public void updateAllAlliesToStartTheContest(HttpServletRequest req){
        Enumeration<String> attributes = req.getSession().getAttributeNames();
        while (attributes.hasMoreElements()) {
            String attribute = (String) attributes.nextElement();
            //need to find the right url of this ally
            try{
                //doGet(req, resp);
                //need to send a message to the curr ally to start working (like pressing the START button)
            }catch (Exception e){

            }
        }
    }
}
