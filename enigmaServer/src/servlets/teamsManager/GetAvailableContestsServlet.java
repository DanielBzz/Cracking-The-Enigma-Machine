package servlets.teamsManager;

import contestDtos.ContestDetailsDTO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.datamanager.ContestsManager;
import servlets.utils.ServletUtils;

import java.util.Set;

@WebServlet(name = "getContestsServlet", urlPatterns = "/teamManager/getContests")
public class GetAvailableContestsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {

        try {
            ContestsManager manager = ServletUtils.getContestManager(request.getServletContext());
            Set<ContestDetailsDTO> contestsDetails =  manager.getContestsDetails();
            ServletUtils.createResponse(response,HttpServletResponse.SC_OK,ServletUtils.GSON_INSTANCE.toJson(contestsDetails));
        }catch (Exception e){
            ServletUtils.createResponse(response,HttpServletResponse.SC_INTERNAL_SERVER_ERROR,e.getMessage());
        }
    }

}
