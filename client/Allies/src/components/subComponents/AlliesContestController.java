package components.subComponents;

import components.AgentsListController;
import components.AlliesListController;
import components.ContestDetailsTableController;
import components.main.AlliesMainAppController;
import contestDtos.ContestDetailsDTO;
import contestDtos.TeamDetailsContestDTO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import util.Constants;
import util.Presenter;

import java.io.IOException;


public class AlliesContestController implements Presenter {
    private AlliesMainAppController parentController;
    @FXML private GridPane alliesDecryptionProgressAndCandidatesComponent;
    @FXML private AlliesDecryptionProgressAndCandidatesController alliesDecryptionProgressAndCandidatesController;
    @FXML private AnchorPane contestDataArea;
    @FXML private AnchorPane alliesTablePlace;
    private AlliesListController alliesTableComponentController;
    @FXML private AnchorPane agentsTablePlace;
    private AgentsListController agentsTableComponentController;
    @FXML private Label totalTasksLabel;
    @FXML private Label producedTasksLabel;
    @FXML private Label finishedTasksLabel;

    public void initial() {

        try {
            FXMLLoader load = new FXMLLoader();
            load.setLocation(AgentsListController.class.getResource("agents-list.fxml"));
            agentsTablePlace.getChildren().add(load.load());
            agentsTableComponentController = load.getController();


            load = new FXMLLoader();
            load.setLocation(AlliesListController.class.getResource("allies-list.fxml"));
            alliesTablePlace.getChildren().add(load.load());
            alliesTableComponentController = load.getController();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        alliesTableComponentController.startListRefresher(Constants.REQUEST_PATH_GET_TEAMS_IN_CONTEST);
        agentsTableComponentController.startListRefresher(constants.Constants.REQUEST_PATH_USERS_UPDATE);

        // not finish
    }

    public void setBasicThingsForContest(TeamDetailsContestDTO responseDetails){
        changeContest(responseDetails.getContestDetails());
        alliesDecryptionProgressAndCandidatesController.initial(responseDetails.getContestDetails());
        totalTasksLabel.setText(String.valueOf(responseDetails.getContestDetails().getTaskSize()));
    }

    public void setAlliesMainAppController(AlliesMainAppController alliesMainAppController) {
        this.parentController = alliesMainAppController;
    }

    public void clearComponent(){
        alliesTableComponentController.cleanTable();
        agentsTableComponentController.cleanTable();
        alliesDecryptionProgressAndCandidatesController.clearController();
        //need to clear also the rest of the component
    }

    public void addProducedTasks(int moreTasks){
        producedTasksLabel.setText(String.valueOf(Integer.parseInt(producedTasksLabel.getText()) + moreTasks));
    }

    public void addFinishedTasks(int moreTasks){
        finishedTasksLabel.setText(String.valueOf(Integer.parseInt(finishedTasksLabel.getText()) + moreTasks));
    }

    private void changeContest(ContestDetailsDTO contestDetails) {

        contestDataArea.getChildren().clear();
        contestDataArea.getChildren().add(new ContestDetailsController(contestDetails));
    }
}