package logic;

import okhttp3.OkHttpClient;

public class Constants {

    public final static OkHttpClient HTTP_CLIENT = new OkHttpClient();
    public final static String BASE_DOMAIN = "localhost";
    private final static String BASE_URL = "http://" + BASE_DOMAIN + ":8080";
    private final static String CONTEXT_PATH = "/enigmaServer";
    private final static String FULL_SERVER_PATH = BASE_URL + CONTEXT_PATH;
    private final static String UBOAT_REQUEST_PATH = FULL_SERVER_PATH + "/uBoat";
    private final static String ALLIES_REQUEST_PATH = FULL_SERVER_PATH + "/allies";
    private final static String AGENT_REQUEST_PATH = FULL_SERVER_PATH + "/agent";

    //- - - - - - - - - - - uBoat requests path - - - - - - - - - - - - - - - //

    public final static String REQUEST_PATH_UBOAT_LOGIN = UBOAT_REQUEST_PATH + "/login";
    public final static String REQUEST_PATH_READ_FILE = UBOAT_REQUEST_PATH + "/loadFile";
    public final static String REQUEST_PATH_INIT_USER_MACHINE = UBOAT_REQUEST_PATH + "/initMachine";
    public final static String REQUEST_PATH_ENCRYPT_MESSAGE = UBOAT_REQUEST_PATH + "/encrypt";
}
