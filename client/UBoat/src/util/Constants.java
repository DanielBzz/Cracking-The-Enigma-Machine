package util;

import com.google.gson.Gson;
import okhttp3.OkHttpClient;

public class Constants {

    // global constants
    public final static int REFRESH_RATE = 2000;

    // fxml locations
    public final static String MAIN_PAGE_FXML_RESOURCE_LOCATION = "/components/uBoat-app-main.fxml";
    public final static String LOGIN_PAGE_FXML_RESOURCE_LOCATION = "/components/login/login.fxml";
    public final static String UBOAT_ROOM_FXML_RESOURCE_LOCATION = "/components/subControllers/uBoat-room-main.fxml";

    // GSON instance
    public final static Gson GSON_INSTANCE = new Gson();

    public final static OkHttpClient HTTP_CLIENT = new OkHttpClient();
    public final static String BASE_DOMAIN = "localhost";
    private final static String BASE_URL = "http://" + BASE_DOMAIN + ":8080";
    private final static String CONTEXT_PATH = "/enigmaServer";

    public final static String UBOAT_CLIENT = "contestManager";
    public final static String ALLIE_CLIENT = "teamManager";

    private final static String FULL_SERVER_PATH = BASE_URL + CONTEXT_PATH;
    private final static String UBOAT_REQUEST_PATH = FULL_SERVER_PATH + "/" + UBOAT_CLIENT;
    private final static String ALLIES_REQUEST_PATH = FULL_SERVER_PATH + "/" + ALLIE_CLIENT;
    private final static String AGENT_REQUEST_PATH = FULL_SERVER_PATH + "/agent";

    public final static String LOGIN_PAGE_GENERAL = "/login";


    //- - - - - - - - - - - uBoat requests path - - - - - - - - - - - - - - - //
    public final static String REQUEST_PATH_LOGIN = FULL_SERVER_PATH + "/login";
    public final static String REQUEST_PATH_READ_FILE = UBOAT_REQUEST_PATH + "/loadFile";
    public final static String REQUEST_PATH_INIT_USER_MACHINE = UBOAT_REQUEST_PATH + "/initMachine";
    public final static String REQUEST_PATH_ENCRYPT_MESSAGE = UBOAT_REQUEST_PATH + "/encrypt";

    //- - - - - - - - - - - Allie requests path - - - - - - - - - - - - - - - //
    public final static String REQUEST_PATH_JOIN_CONTEST = ALLIES_REQUEST_PATH + "/joinToContest";

}
