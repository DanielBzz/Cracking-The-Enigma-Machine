package util;

import static constants.Constants.FULL_SERVER_PATH;

public class Constants {
    public final static String AGENT_CLIENT = "agent";
    public final static String AGENT_MAIN_APP_FXML_RESOURCE_LOCATION = "/components/main/agent-main-app.fxml";
    public final static String AGENT_ENTER_DETAILS_FXML_RESOURCE_LOCATION = "/components/subComponents/entering-agent-details.fxml";
    private final static String AGENT_REQUEST_PATH = FULL_SERVER_PATH + "/" + AGENT_CLIENT;
    public final static String REQUEST_PATH_ADD_AGENT_TO_TEAM = AGENT_REQUEST_PATH + "/addAgentToTeam";
}