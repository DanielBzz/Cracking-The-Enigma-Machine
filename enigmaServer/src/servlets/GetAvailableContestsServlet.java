package servlets;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "getContestsServlet", urlPatterns = "/teamManager/getContests")
public class GetAvailableContestsServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) {




    }

}
