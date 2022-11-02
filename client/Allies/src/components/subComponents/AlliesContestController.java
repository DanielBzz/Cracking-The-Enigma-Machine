package components.subComponents;

import components.ActivePlayerListController;
import components.AgentsListController;
import components.ContestDetailsController;
import components.DynamicComponent;
import components.main.AlliesMainAppController;
import contestDtos.ContestDetailsDTO;
import contestDtos.TeamDetailsContestDTO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import util.Constants;
import util.Presenter;
import util.RefresherController;
import util.tableHolderInterfaces.Disconnectable;

import java.io.IOException;

import static util.Constants.REQUEST_PATH_GET_PROGRESS_DATA;


public class AlliesContestController extends RefresherController implements Presenter, Disconnectable {
    private AlliesMainAppController parentController;
    @FXML private GridPane alliesDecryptionProgressAndCandidatesComponent;
    @FXML private AlliesDecryptionProgressAndCandidatesController alliesDecryptionProgressAndCandidatesComponentController;
    @FXML private AnchorPane contestDataAreaComponent;
    private ContestDetailsController contestDataAreaComponentController;
    @FXML private AnchorPane alliesTablePlace;
    private ActivePlayerListController alliesTableComponentController;
    @FXML private AnchorPane agentsTablePlace;
    private AgentsListController agentsTableComponentController;
    @FXML private ScrollPane progressDataComponent;
    @FXML private ProgressDataController progressDataComponentController;

    public void initial() {

        if(alliesDecryptionProgressAndCandidatesComponentController!=null){
            alliesDecryptionProgressAndCandidatesComponentController.setAlliesContestController(this);
        }

        if(progressDataComponentController!=null){
            progressDataComponentController.setAlliesContestController(this);
            progressDataComponentController.initial();
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

        progressDataComponentController.updateTotalTasksLabel((int) responseDetails.getContestDetails().getTaskSize());
    }

    public void setAlliesMainAppController(AlliesMainAppController alliesMainAppController) {
        this.parentController = alliesMainAppController;
    }

    public void clearComponent(){
        System.out.println("============================clearing all contest component after contest =====================");
        alliesTableComponentController.clearComponent();
        agentsTableComponentController.cleanTable();
        alliesDecryptionProgressAndCandidatesComponentController.clearController();
        contestDataAreaComponentController.clearComponent();
        progressDataComponentController.clearComponent();
    }

    public void showPopUpMessage(String msg) {
        System.out.println("======================================need to print winner nowww");
        new Alert(Alert.AlertType.INFORMATION,msg, ButtonType.OK).showAndWait();
    }

    public void setActive(){
        alliesTableComponentController.startListRefresher(Constants.REQUEST_PATH_GET_TEAMS_IN_CONTEST);
        agentsTableComponentController.startListRefresher(constants.Constants.REQUEST_PATH_USERS_UPDATE);
        startListRefresher(constants.Constants.REQUEST_PATH_IS_CONTEST_ON);
    }

    public void setInactive(){
        alliesTableComponentController.stopListRefresher();
        agentsTableComponentController.stopListRefresher();
        alliesDecryptionProgressAndCandidatesComponentController.inFinishedContest();
        stopListRefresher();
    }

    @Override
    public void updateList(String jsonUserList) {

        ContestDetailsDTO contestData = constants.Constants.GSON_INSTANCE.fromJson(jsonUserList, ContestDetailsDTO.class);

        contestDataAreaComponentController.initial(contestData);
        alliesDecryptionProgressAndCandidatesComponentController.updateEncryptedMessage(contestData.getEncryptedMessage());

        if(contestData.isStatus()){
            stopListRefresher();
        }
    }

    public void startProgressListener(){
        progressDataComponentController.startListRefresher(REQUEST_PATH_GET_PROGRESS_DATA);
    }

    public void stopProgressRefresher(){
        progressDataComponentController.stopListRefresher();
    }

    public void backToDashboard() {
        parentController.contestInactive();
    }
}