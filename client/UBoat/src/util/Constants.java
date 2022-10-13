package util;

import okhttp3.OkHttpClient;

import static constants.Constants.FULL_SERVER_PATH;

public class Constants {

    public final static String UBOAT_MAIN_APP_FXML_RESOURCE_LOCATION = "/UBoat/components/main/uBoatMainApp.fxml";

    public final static OkHttpClient HTTP_CLIENT = new OkHttpClient();      // delete from here ... only one in generalClient/util

    //- - - - - - - - - - - uBoat requests path - - - - - - - - - - - - - - - //
    public final static String UBOAT_CLIENT = "contestManager";
    private final static String UBOAT_REQUEST_PATH = FULL_SERVER_PATH + "/" + UBOAT_CLIENT;
    public final static String REQUEST_PATH_READ_FILE = UBOAT_REQUEST_PATH + "/loadFile";
    public final static String REQUEST_PATH_INIT_USER_MACHINE = UBOAT_REQUEST_PATH + "/initMachine";
    public final static String REQUEST_PATH_ENCRYPT_MESSAGE = UBOAT_REQUEST_PATH + "/encrypt";


    //- - - - - - - - - - - Allie requests path - - - - - - - - - - - - - - - //
    public final static String ALLIE_CLIENT = "teamManager";
    private final static String ALLIES_REQUEST_PATH = FULL_SERVER_PATH + "/" + ALLIE_CLIENT;
    public final static String REQUEST_PATH_JOIN_CONTEST = ALLIES_REQUEST_PATH + "/joinToContest";

    //- - - - - - - - - - - Agent requests path - - - - - - - - - - - - - - - //

    private final static String AGENT_REQUEST_PATH = FULL_SERVER_PATH + "/agent";

}
