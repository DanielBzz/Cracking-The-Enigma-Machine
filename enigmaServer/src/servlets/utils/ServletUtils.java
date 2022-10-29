package servlets.utils;

import com.google.gson.Gson;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import logic.datamanager.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Scanner;

public class ServletUtils {

    public final static Gson GSON_INSTANCE = new Gson();
    public static final String MESSAGE_PARAMETER = "message";
    public static final String CONTEST_NAME_PARAMETER = "contestName";
    public static final String CONTEST_MANAGER_ATTRIBUTE_NAME = "contestManager";
    public static final String TEAM_MANAGER_ATTRIBUTE_NAME = "teamManager";
    public static final String AGENT_ATTRIBUTE_NAME = "agentManager";
    public static final String CANDIDATES_MANAGER_ATTRIBUTE_NAME = "candidatesManager";

    private static final Object contestManagerLock = new Object();
    private static final Object teamManagerLock = new Object();
    private static final Object agentManagerLock = new Object();
    private static final Object candidatesManagerLock = new Object();

    public static DataManager getDataManager(ServletContext servletContext, String attributeName) throws Exception {

        if(attributeName.equals(CONTEST_MANAGER_ATTRIBUTE_NAME)){
            return getContestManager(servletContext);
        }
        else if(attributeName.equals(TEAM_MANAGER_ATTRIBUTE_NAME)) {
            return getTeamsManager(servletContext);
        }else if(attributeName.equals(AGENT_ATTRIBUTE_NAME)) {
            return getAgentManager(servletContext);
        }else{
            throw new Exception("You should declare your " + SessionUtils.ACCESS_ATTRIBUTE + ":" + CONTEST_MANAGER_ATTRIBUTE_NAME + "/" + TEAM_MANAGER_ATTRIBUTE_NAME);
        }
    }

    public static ContestsManager getContestManager(ServletContext servletContext){

        synchronized (contestManagerLock){
            if(servletContext.getAttribute(CONTEST_MANAGER_ATTRIBUTE_NAME) == null){
                servletContext.setAttribute(CONTEST_MANAGER_ATTRIBUTE_NAME,new ContestsManager());
            }
        }

        return (ContestsManager)servletContext.getAttribute(CONTEST_MANAGER_ATTRIBUTE_NAME);
    }

    public static TeamsManager getTeamsManager(ServletContext servletContext){

        synchronized (teamManagerLock){
            if(servletContext.getAttribute(TEAM_MANAGER_ATTRIBUTE_NAME) == null){
                servletContext.setAttribute(TEAM_MANAGER_ATTRIBUTE_NAME,new TeamsManager());
            }
        }

        return (TeamsManager)servletContext.getAttribute(TEAM_MANAGER_ATTRIBUTE_NAME);
    }

    public static AgentManager getAgentManager(ServletContext servletContext){

        synchronized (agentManagerLock){
            if(servletContext.getAttribute(AGENT_ATTRIBUTE_NAME) == null){
                servletContext.setAttribute(AGENT_ATTRIBUTE_NAME,new AgentManager());
            }
        }

        return (AgentManager) servletContext.getAttribute(AGENT_ATTRIBUTE_NAME);
    }

    public static CandidatesManager getCandidatesManager(ServletContext servletContext){

        synchronized (candidatesManagerLock){
            if(servletContext.getAttribute(CANDIDATES_MANAGER_ATTRIBUTE_NAME) == null){
                servletContext.setAttribute(CANDIDATES_MANAGER_ATTRIBUTE_NAME,new CandidatesManager());
            }
        }

        return (CandidatesManager)servletContext.getAttribute(CANDIDATES_MANAGER_ATTRIBUTE_NAME);
    }

    public static void createResponse(HttpServletResponse resp, int status, String msg){

        resp.setContentType("text/plain;charset=UTF-8");
        resp.setStatus(status);
        if(msg != null) {
            try {
                resp.getOutputStream().print(msg);
                resp.setContentLength(msg.length());
                resp.getOutputStream().close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static String getBody(Collection<Part> fileParts) throws ServletException, IOException {

        StringBuilder fileContent = new StringBuilder();

        fileParts.forEach(part -> {
            try {
                fileContent.append(readFromInputStream(part.getInputStream()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        return fileContent.toString();
    }

    private static String readFromInputStream(InputStream inputStream) {
        return new Scanner(inputStream).useDelimiter("\\Z").next();
    }
}
