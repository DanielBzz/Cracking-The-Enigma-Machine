package logic;

public class Constants {

    public final static String BASE_DOMAIN = "localhost";
    private final static String BASE_URL = "http://" + BASE_DOMAIN + ":8080";

    private final static String UBOAT_REQUEST_PATH = BASE_URL + "/uBoat";
    private final static String ALLIES_REQUEST_PATH = BASE_URL + "/allies";
    private final static String AGENT_REQUEST_PATH = BASE_URL + "/agent";

    public final static String LOGIN_PAGE = UBOAT_REQUEST_PATH + "/login";
    public final static String READ_FILE_PAGE = UBOAT_REQUEST_PATH + "/loadFile";


}
