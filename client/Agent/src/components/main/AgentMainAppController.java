package components.main;

import components.CandidatesTableController;
import components.subComponents.AgentProgressAndStatusController;
import components.subComponents.ContestAndTeamDataController;
import contestDtos.AgentProgressDTO;
import javafx.beans.property.IntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import logic.AgentLogic;
import mainapp.AppMainController;
import mainapp.ClientMainController;
import util.Constants;

public class AgentMainAppController implements AppMainController {
    private ClientMainController parentController;
    private AgentLogic agentLogic;

    @FXML
    private ScrollPane contestAndTeamDataComponent;
    @FXML
    private ContestAndTeamDataController contestAndTeamDataComponentController;

    @FXML
    private ScrollPane agentProgressAndStatusComponent;
    @FXML
    private AgentProgressAndStatusController agentProgressAndStatusComponentController;

    @FXML
    private ScrollPane agentsCandidatesComponent;
    @FXML
    private CandidatesTableController agentsCandidatesComponentController;

    @FXML
    public void initialize(){
        agentLogic = new AgentLogic(this, , );
        if(contestAndTeamDataComponentController!= null) {
            contestAndTeamDataComponentController.setAgentMainAppController(this);
            contestAndTeamDataComponentController.setTeamNameLabel();
        }
        if(agentProgressAndStatusComponentController!= null){
            agentProgressAndStatusComponentController.setAgentMainAppController(this);
        }
        if(agentsCandidatesComponentController!= null){
            agentsCandidatesComponentController.setWhoFoundTheAnswerLabel("agent");
        }
    }

    public void updateTasksData(AgentProgressDTO data){
        agentProgressAndStatusComponentController.updateData(data);
    }

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

    public void setActive(){
        //agentProgressAndStatusComponentController.startListRefresher(Constants.REQUEST_PATH_GET_TASKS_DATA);
        //contestAndTeamDataComponentController.startListRefresher(Constants.REQUEST_PATH_GET_CONTESTS);
        agentsCandidatesComponentController.startListRefresher();
    }

    public void setPassive(){
        //agentProgressAndStatusComponentController.stopListRefresher();
        agentsCandidatesComponentController.cancelRefresher();
    }
}
