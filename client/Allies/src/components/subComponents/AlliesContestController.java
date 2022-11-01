package components.subComponents;

import components.ActivePlayerListController;
import components.AgentsListController;
import components.ContestDetailsController;
import components.DynamicComponent;
import components.main.AlliesMainAppController;
import contestDtos.TeamDetailsContestDTO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import util.Constants;
import util.Presenter;
import util.tableHolderInterfaces.Disconnectable;

import java.io.IOException;


public class AlliesContestController implements Presenter, Disconnectable {
    private AlliesMainAppController parentController;
    @FXML private GridPane alliesDecryptionProgressAndCandidatesComponent;
    @FXML private AlliesDecryptionProgressAndCandidatesController alliesDecryptionProgressAndCandidatesComponentController;
    @FXML private AnchorPane contestDataAreaComponent;
    private ContestDetailsController contestDataAreaComponentController;
    @FXML private AnchorPane alliesTablePlace;
    private ActivePlayerListController alliesTableComponentController;
    @FXML private AnchorPane agentsTablePlace;
    private AgentsListController agentsTableComponentController;
    @FXML private Label totalTasksLabel;
    @FXML private Label producedTasksLabel;
    @FXML private Label finishedTasksLabel;

    public void initial() {

        if(alliesDecryptionProgressAndCandidatesComponentController!=null){
            alliesDecryptionProgressAndCandidatesComponentController.setAlliesContestController(this);
        }

        try {
            FXMLLoader load = new FXMLLoader();
            load.setLocation(AgentsListController.class.getResource("agents-list.fxml"));
            Node agentComponent = load.load();
            agentsTablePlace.getChildren().add(agentComponent);
            DynamicComponent.fitToPane(agentComponent);
            agentsTableComponentController = load.getController();

            load = new FXMLLoader();
            load.setLocation(ContestDetailsController.class.getResource("contest-details.fxml"));
            Node contestComponent = load.load();
            contestDataAreaComponent.getChildren().add(contestComponent);
            DynamicComponent.fitToPane(contestComponent);
            contestDataAreaComponentController = load.getController();

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

    }

    @Override
    public void disconnectFromContest(){
        parentController.contestInactive();
        clearComponent();
    }

    public void setNewContestForAllie(TeamDetailsContestDTO responseDetails){
        contestDataAreaComponentController.initial(responseDetails.getContestDetails());
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
        contestDataAreaComponentController.clearComponent();
    }

    public void addProducedTasks(int moreTasks){
        producedTasksLabel.setText(String.valueOf(Integer.parseInt(producedTasksLabel.getText()) + moreTasks));
    }

    public void addFinishedTasks(int moreTasks){
        finishedTasksLabel.setText(String.valueOf(Integer.parseInt(finishedTasksLabel.getText()) + moreTasks));
    }

    public void sowPopUpMessage(String msg) {

        new Alert(Alert.AlertType.INFORMATION,msg, ButtonType.OK);
    }

    public void setActive(){
        alliesTableComponentController.startListRefresher(Constants.REQUEST_PATH_GET_TEAMS_IN_CONTEST);
        agentsTableComponentController.startListRefresher(constants.Constants.REQUEST_PATH_USERS_UPDATE);
    }

    public void setInactive(){
        alliesTableComponentController.stopListRefresher();
        agentsTableComponentController.stopListRefresher();
        alliesDecryptionProgressAndCandidatesComponentController.inFinishedContest();
    }
}