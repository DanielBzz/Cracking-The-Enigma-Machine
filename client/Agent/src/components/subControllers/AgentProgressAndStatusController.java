package components.subControllers;

import components.main.AgentMainAppController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class AgentProgressAndStatusController {


    private AgentMainAppController parentController;
    @FXML
    private Label tasksInQueueLabel;

    @FXML
    private Label totalTakenTasksLabel;

    @FXML
    private Label totalFinishedTasksLabel;

    @FXML
    private Label totalCandidatesLabel;


    public void setAgentMainAppController(AgentMainAppController agentMainAppController) {
        this.parentController = agentMainAppController;
    }

    public void updateData(){

    }

}
