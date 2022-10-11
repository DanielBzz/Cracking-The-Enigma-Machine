package util;

import com.google.gson.Gson;
import okhttp3.OkHttpClient;

public class Constants {

    // global constants
    public final static String LINE_SEPARATOR = System.getProperty("line.separator");
    public final static int REFRESH_RATE = 2000;

    // fxml locations
    //public final static String MAIN_PAGE_FXML_RESOURCE_LOCATION = "/chat/client/component/main/chat-app-main.fxml";

    public final static String MAIN_PAGE_FXML_RESOURCE_LOCATION = "/components/uBoat-app-main.fxml";
    public final static String LOGIN_PAGE_FXML_RESOURCE_LOCATION = "/components/login/login.fxml";
    //public final static String CHAT_ROOM_FXML_RESOURCE_LOCATION = "/chat/component/chatroom/chat-room-main.fxml";
    public final static String UBOAT_ROOM_FXML_RESOURCE_LOCATION = "/components/subControllers/uBoat-room-main.fxml";

    // GSON instance
    public final static Gson GSON_INSTANCE = new Gson();






    public final static OkHttpClient HTTP_CLIENT = new OkHttpClient();
    public final static String BASE_DOMAIN = "localhost";
    private final static String BASE_URL = "http://" + BASE_DOMAIN + ":8080";
    private final static String CONTEXT_PATH = "/enigmaServer_Web_exploded";
    private final static String FULL_SERVER_PATH = BASE_URL + CONTEXT_PATH;
    private final static String UBOAT_REQUEST_PATH = FULL_SERVER_PATH + "/uBoat";
    private final static String ALLIES_REQUEST_PATH = FULL_SERVER_PATH + "/allies";
    private final static String AGENT_REQUEST_PATH = FULL_SERVER_PATH + "/agent";

    public final static String LOGIN_PAGE = UBOAT_REQUEST_PATH + "/login";
    public final static String LOGIN_PAGE_GENERAL = "/login";
    public final static String READ_FILE_PAGE = UBOAT_REQUEST_PATH + "/loadFile";
    public final static String INIT_USER_MACHINE = UBOAT_REQUEST_PATH + "/initMachine";
}
