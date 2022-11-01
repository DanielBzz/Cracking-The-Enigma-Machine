package components;

import contestDtos.ContestDetailsDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import static constants.Constants.*;


public class ContestDetailsController {

    @FXML private Label battleFieldLabel;
    @FXML private Label contestManagerLabel;
    @FXML private Label statusLabel;
    @FXML private Label levelLabel;
    @FXML private Label teamsLabel;
    private int maxAmountOfTeams;
    private int currentAmountOfTeams;

    public void initial(ContestDetailsDTO contestDetails) {
        battleFieldLabel.setText(contestDetails.getBattleFieldName());
        contestManagerLabel.setText(contestDetails.getContestManagerName());
        statusLabel.setText(contestDetails.isStatus() ? ACTIVE_CONTEST : WAITING_CONTEST);
        levelLabel.setText(contestDetails.getLevel());
        maxAmountOfTeams = contestDetails.getTeams().getValue();
        currentAmountOfTeams = contestDetails.getTeams().getKey();
        teamsLabel.setText(String.valueOf(contestDetails.getTeams().getKey()) + FROM_SEPARATOR + String.valueOf(contestDetails.getTeams().getValue()));
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

    public Label getStatusLabel() {
        return statusLabel;
    }

    public void clearComponent() {
        battleFieldLabel.setText("");
        contestManagerLabel.setText("");
        statusLabel.setText("");
        levelLabel.setText("");
        teamsLabel.setText("");

    }
}
