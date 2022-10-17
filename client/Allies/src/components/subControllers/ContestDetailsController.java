package components.subControllers;

import contestDtos.ContestDetailsDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import static constants.Constants.FROM_SEPARATOR;
import static util.Constants.*;

public class ContestDetailsController extends Pane {

    @FXML
    private Label battleFieldLabel;

    @FXML
    private Label contestManagerLabel;

    @FXML
    private Label statusLabel;

    @FXML
    private Label levelLabel;

    @FXML
    private Label teamsLabel;

    private int maxAmountOfTeams;
    private int currentAmountOfTeams;
    private int taskSize;

    public ContestDetailsController(ContestDetailsDTO contestDetails){
        battleFieldLabel.setText(contestDetails.getBattleFieldName());
        contestManagerLabel.setText(contestDetails.getContestManagerName());
        statusLabel.setText(contestDetails.isStatus() ? ACTIVE_CONTEST : WAITING_CONTEST);
        levelLabel.setText(contestDetails.getLevel());
        maxAmountOfTeams = contestDetails.getTeams().getValue();
        currentAmountOfTeams = contestDetails.getTeams().getKey();
        teamsLabel.setText(String.valueOf(contestDetails.getTeams().getKey()) + FROM_SEPARATOR + String.valueOf(contestDetails.getTeams().getValue()));
        taskSize = contestDetails.getTaskSize();
    }

    public void setStatusLabel(Label statusLabel) {
        this.statusLabel = statusLabel;
    }

    public void addTeam(){
        currentAmountOfTeams++;
        teamsLabel.setText(currentAmountOfTeams + FROM_SEPARATOR + maxAmountOfTeams);
    }

    public void deleteTeam(){
        currentAmountOfTeams--;
        teamsLabel.setText(currentAmountOfTeams + FROM_SEPARATOR + maxAmountOfTeams);
    }

    public String getContestManagerName(){
        return contestManagerLabel.getText();
    }

    public int getTaskSize(){
        return taskSize;
    }

    public Label getStatusLabel() {
        return statusLabel;
    }
}
