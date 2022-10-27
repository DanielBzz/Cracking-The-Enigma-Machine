package components.subComponents;

import components.AgentsListController;
import components.AlliesListController;
import components.main.AlliesMainAppController;
import contestDtos.ContestDetailsDTO;
import contestDtos.TeamDetailsContestDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import util.Constants;
import util.Presenter;


public class AlliesContestController implements Presenter {
    private AlliesMainAppController parentController;
    @FXML private GridPane alliesDecryptionProgressAndCandidatesComponent;
    @FXML private AlliesDecryptionProgressAndCandidatesController alliesDecryptionProgressAndCandidatesController;
    @FXML private AnchorPane contestDataArea;
    @FXML private ScrollPane alliesTableComponent;
    @FXML private AlliesListController alliesTableComponentController;
    @FXML private ScrollPane agentsTableComponent;
    @FXML private AgentsListController agentsTableComponentController;
    @FXML private Label totalTasksLabel;
    @FXML private Label producedTasksLabel;
    @FXML private Label finishedTasksLabel;


    public void setAlliesMainAppController(AlliesMainAppController alliesMainAppController) {
        this.parentController = alliesMainAppController;
    }

    public void clearComponent(){
        alliesTableComponentController.cleanTable();
        agentsTableComponentController.cleanTable();
        alliesDecryptionProgressAndCandidatesController.clearController();
        //need to clear also the rest of the component
    }

    public void initial(){
        //use the get contest info (
    }

    public void updateLevel(String level){
        alliesDecryptionProgressAndCandidatesController.updateLevel(level);
    }

    public void updateAmountOfAgents(){
        alliesDecryptionProgressAndCandidatesController.updateAmountOfAgents(parentController.getAmountOfAgents());
    }

    public double getTaskSize(){
        return 1;
    }

    public void setTotalTasksLabel(int totalTasks){
        totalTasksLabel.setText(String.valueOf(totalTasks));
    }

    public void addProducedTasks(int moreTasks){
        producedTasksLabel.setText(String.valueOf(Integer.parseInt(producedTasksLabel.getText()) + moreTasks));
    }

    public void addFinishedTasks(int moreTasks){
        finishedTasksLabel.setText(String.valueOf(Integer.parseInt(finishedTasksLabel.getText()) + moreTasks));
    }

    public String getContestName(){
        ContestDetailsController contestDetails = (ContestDetailsController) contestDataArea.getChildren();
        return contestDetails.getContestManagerName();
    }

    public void activateContestScreen(TeamDetailsContestDTO responseDetails) {

        alliesTableComponentController.startListRefresher(Constants.REQUEST_PATH_GET_TEAMS_IN_CONTEST);
        agentsTableComponentController.startListRefresher(constants.Constants.REQUEST_PATH_USERS_UPDATE);
        changeContest(responseDetails.getContestDetails());
        // not finish
    }

    private void changeContest(ContestDetailsDTO contestDetails) {

        contestDataArea.getChildren().clear();
        contestDataArea.getChildren().add(new ContestDetailsController(contestDetails));
    }
}