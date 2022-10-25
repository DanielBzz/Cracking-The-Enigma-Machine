package components.main;

import components.CandidatesTableController;
import components.subControllers.AgentProgressAndStatusController;
import components.subControllers.ContestAndTeamDataController;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

public class AgentMainAppController {
    //need to hold agentLogic
    @FXML
    private ScrollPane contestAndTeamDataComponent;
    @FXML
    private ContestAndTeamDataController contestAndTeamDataComponentController;

    @FXML
    private GridPane agentProgressAndStatusComponent;
    @FXML
    private AgentProgressAndStatusController agentProgressAndStatusComponentController;

    @FXML
    private AnchorPane agentsCandidatesComponent;
    @FXML
    private CandidatesTableController agentsCandidatesComponentController;


}
