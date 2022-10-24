package util;

import static constants.Constants.FULL_SERVER_PATH;

public class Constants {

    public final static String ALLIES_MAIN_APP_FXML_RESOURCE_LOCATION = "/components/main/AlliesMainApp.fxml";

    public final static String ACTIVE_CONTEST = "active";
    public final static String WAITING_CONTEST = "waiting";
    public final static String ALLIES_CLIENT = "teamManager";

    private final static String ALLIES_REQUEST_PATH = FULL_SERVER_PATH + "/" + ALLIES_CLIENT;
    public final static String REQUEST_PATH_JOIN_TO_CONTEST = ALLIES_REQUEST_PATH + "/joinToContest";
    public final static String REQUEST_PATH_SET_READY = ALLIES_REQUEST_PATH + "/SetTeamReady";

}
