package components.main;

import components.CandidatesTableController;
import components.subControllers.AgentProgressAndStatusController;
import components.subControllers.ContestAndTeamDataController;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import logic.AgentLogic;
import mainapp.AppMainController;
import mainapp.ClientMainController;

public class AgentMainAppController implements AppMainController {
    private ClientMainController parentController;
    private AgentLogic agentLogic;

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


    @Override
    public void setClientMainController(ClientMainController clientMainController) {
        this.parentController = clientMainController;
    }

    @Override
    public void close() {
        agentLogic.logOut();
        clearComponent();
        parentController.switchToLogin();
    }

    @Override
    public void clearComponent() {
        contestAndTeamDataComponentController.clearTable();
        agentProgressAndStatusComponentController.clearDetails();
        agentsCandidatesComponentController.clear();
    }

    @Override
    public void loadClientMainPage() {

    }
}
