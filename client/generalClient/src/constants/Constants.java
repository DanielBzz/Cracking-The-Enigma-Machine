package constants;

import com.google.gson.Gson;

public class Constants {

    public final static int REFRESH_RATE = 2000;
    public final static Gson GSON_INSTANCE = new Gson();

    // fxml locations
    public final static String MAIN_PAGE_FXML_RESOURCE_LOCATION = "/mainapp/generalMainComponent.fxml";
    public final static String LOGIN_PAGE_FXML_RESOURCE_LOCATION = "../login/login.fxml";

    public final static String BASE_DOMAIN = "localhost";
    private final static String BASE_URL = "http://" + BASE_DOMAIN + ":8080";
    private final static String CONTEXT_PATH = "/enigmaServer";
    public final static String FULL_SERVER_PATH = BASE_URL + CONTEXT_PATH;

    public final static String REQUEST_PATH_LOGIN = FULL_SERVER_PATH + "/login";
    public final static String REQUEST_PATH_LOGOUT = FULL_SERVER_PATH + "/logout";
    public final static String REQUEST_PATH_USERS_UPDATE = FULL_SERVER_PATH + "/getUsers";
    public final static String REQUEST_PATH_GET_CANDIDATES = FULL_SERVER_PATH + "/getCandidates";



    public final static String ALLIES_TYPE = "allies";
    public final static String AGENT_TYPE = "agent";

    public final static String ACTIVE_CONTEST = "active";
    public final static String WAITING_CONTEST = "waiting";

    public final static String UBOAT_HEADER = "UBoat Player";
    public final static String ALLIES_HEADER = "Allies Player";
    public final static String AGENT_HEADER = "Agent Player";


    public final static String AGENT_IN_CONTEST_TYPE = "agent in contest";
    public final static String FROM_SEPARATOR = " / ";

}
