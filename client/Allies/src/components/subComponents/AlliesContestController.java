package components.subComponents;

import components.AgentsListController;
import components.ActivePlayerListController;
import components.DynamicComponent;
import components.main.AlliesMainAppController;
import contestDtos.ContestDetailsDTO;
import contestDtos.TeamDetailsContestDTO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import util.Constants;
import util.Presenter;
import util.tableHolderInterfaces.TeamTableHolder;

import java.io.IOException;


public class AlliesContestController implements Presenter, TeamTableHolder {
    private AlliesMainAppController parentController;
    @FXML private GridPane alliesDecryptionProgressAndCandidatesComponent;
    @FXML private AlliesDecryptionProgressAndCandidatesController alliesDecryptionProgressAndCandidatesComponentController;
    @FXML private BorderPane contestDataAreaComponent;
    @FXML private ContestDetailsController contestDataAreaComponentController;
    @FXML private AnchorPane alliesTablePlace;
    private ActivePlayerListController alliesTableComponentController;
    @FXML private AnchorPane agentsTablePlace;
    private AgentsListController agentsTableComponentController;
    @FXML private Label totalTasksLabel;
    @FXML private Label producedTasksLabel;
    @FXML private Label finishedTasksLabel;

    public void initial() {

        try {
            FXMLLoader load = new FXMLLoader();
            load.setLocation(AgentsListController.class.getResource("agents-list.fxml"));
            Node agentComponent = load.load();
            agentsTablePlace.getChildren().add(agentComponent);
            DynamicComponent.fitToPane(agentComponent);
            agentsTableComponentController = load.getController();



            load = new FXMLLoader();
            load.setLocation(ActivePlayerListController.class.getResource("activePlayerList.fxml"));
            Node alliesComponent = load.load();
            alliesTablePlace.getChildren().add(alliesComponent);
            DynamicComponent.fitToPane(alliesComponent);
            alliesTableComponentController = load.getController();
            alliesTableComponentController.setTableHolder(this);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

        // not finish
    }

    public void setBasicThingsForContest(TeamDetailsContestDTO responseDetails){
        changeContest(responseDetails.getContestDetails());
        alliesDecryptionProgressAndCandidatesComponentController.initial(responseDetails.getContestDetails());
        totalTasksLabel.setText(String.valueOf(responseDetails.getContestDetails().getTaskSize()));
    }

    public void setAlliesMainAppController(AlliesMainAppController alliesMainAppController) {
        this.parentController = alliesMainAppController;
    }

    public void clearComponent(){
        alliesTableComponentController.clearComponent();
        agentsTableComponentController.cleanTable();
        alliesDecryptionProgressAndCandidatesComponentController.clearController();
        //need to clear also the rest of the component
    }

    public void addProducedTasks(int moreTasks){
        producedTasksLabel.setText(String.valueOf(Integer.parseInt(producedTasksLabel.getText()) + moreTasks));
    }

    public void addFinishedTasks(int moreTasks){
        finishedTasksLabel.setText(String.valueOf(Integer.parseInt(finishedTasksLabel.getText()) + moreTasks));
    }

    private void changeContest(ContestDetailsDTO contestDetails) {
        
        contestDataAreaComponentController.initial(contestDetails);
    }

    public void popUpMessage(String msg) {

        new Alert(Alert.AlertType.INFORMATION,msg, ButtonType.OK);
    }

    public void setActive(){
        alliesTableComponentController.startListRefresher(Constants.REQUEST_PATH_GET_TEAMS_IN_CONTEST);
        agentsTableComponentController.startListRefresher(constants.Constants.REQUEST_PATH_USERS_UPDATE);
    }

    public void setInactive(){
        alliesTableComponentController.stopListRefresher();
        agentsTableComponentController.stopListRefresher();
    }

}