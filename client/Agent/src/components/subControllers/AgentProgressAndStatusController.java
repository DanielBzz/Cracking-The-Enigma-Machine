package components.subControllers;

import components.main.AgentMainAppController;
import contestDtos.AgentProgressDTO;
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

    public void updateData(AgentProgressDTO agentProgressDTO){
        this.tasksInQueueLabel.setText(String.valueOf(agentProgressDTO.getTasksInQueue()));
        this.totalTakenTasksLabel.setText(String.valueOf(agentProgressDTO.getTotalTakenTasks()));
        this.totalFinishedTasksLabel.setText(String.valueOf(agentProgressDTO.getTotalFinishedTasks()));
        this.totalCandidatesLabel.setText(String.valueOf(agentProgressDTO.getTotalAmountOfCandidates()));
    }

}
