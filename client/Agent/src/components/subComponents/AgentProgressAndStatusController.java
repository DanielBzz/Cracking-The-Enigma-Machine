package components.subComponents;

import components.main.AgentMainAppController;
import constants.Constants;
import contestDtos.AgentProgressDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import util.RefresherController;

import java.util.concurrent.atomic.AtomicInteger;

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

    public synchronized void updateData(AgentProgressDTO agentProgressDTO){
        this.tasksInQueueLabel.setText(String.valueOf(agentProgressDTO.getTasksInQueue()));
        this.totalTakenTasksLabel.setText(String.valueOf(agentProgressDTO.getTotalTakenTasks()));
        this.totalFinishedTasksLabel.setText(String.valueOf(agentProgressDTO.getTotalFinishedTasks()));
        this.totalCandidatesLabel.setText(String.valueOf(agentProgressDTO.getTotalAmountOfCandidates()));

    }

    public void clearDetails(){
        this.tasksInQueueLabel.setText("");
        this.totalTakenTasksLabel.setText("");
        this.totalFinishedTasksLabel.setText("");
        this.totalCandidatesLabel.setText("");
    }

//    @Override
//    public void updateList(String jsonUserList) {
//
//
//        AgentProgressDTO newDetails = Constants.GSON_INSTANCE.fromJson(jsonUserList, AgentProgressDTO.class);
//        updateData(newDetails);
//    }
}
