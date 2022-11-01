package components.subComponents;

import com.sun.istack.internal.NotNull;
import components.ActivePlayerListController;
import components.ContestDetailsTableController;
import components.DynamicComponent;
import components.main.AlliesMainAppController;
import contestDtos.TeamDetailsContestDTO;
import http.HttpClientUtil;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import okhttp3.*;
import util.Constants;
import util.tableHolderInterfaces.AgentTableHolder;

import java.io.IOException;

import static util.Constants.REQUEST_PATH_JOIN_TO_CONTEST;


public class AlliesDashboardController implements AgentTableHolder {

    private AlliesMainAppController parentController;
    @FXML private AnchorPane agentsPlace;
    private ActivePlayerListController agentsTableComponentController;
    @FXML private AnchorPane contestDetailsPlace;
    private ContestDetailsTableController contestTableComponentController;
    @FXML private Button joinButton;

    public void initial(){
        try {
            FXMLLoader load = new FXMLLoader();
            load.setLocation(ActivePlayerListController.class.getResource("activePlayerList.fxml"));
            Node agentComponent = load.load();
            agentsPlace.getChildren().add(agentComponent);
            DynamicComponent.fitToPane(agentComponent);
            agentsTableComponentController = load.getController();
            agentsTableComponentController.setTableHolder(this);

            load = new FXMLLoader();
            load.setLocation(ContestDetailsTableController.class.getResource("contest-details-table.fxml"));
            Node contestsComponent = load.load();
            contestDetailsPlace.getChildren().add(contestsComponent);
            DynamicComponent.fitToPane(contestsComponent);
            contestTableComponentController = load.getController();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
    public void setAlliesMainAppController(AlliesMainAppController alliesMainAppController) {
        this.parentController = alliesMainAppController;
    }

    public void setActive(){
        contestTableComponentController.startListRefresher(Constants.REQUEST_PATH_GET_CONTESTS);
        agentsTableComponentController.startListRefresher(constants.Constants.REQUEST_PATH_USERS_UPDATE);
    }

    public void setInactive(){
        contestTableComponentController.stopListRefresher();
        agentsTableComponentController.stopListRefresher();
    }

    public void clearComponent(){
        agentsTableComponentController.clearComponent();
        contestTableComponentController.cleanTable();
    }

    @FXML
    void joinButtonListener(ActionEvent event) {

        String contestManagerName = contestTableComponentController.getChosenContestUserName();

        if(contestManagerName == null){
            new Alert(Alert.AlertType.WARNING, "Contest not chosen", ButtonType.OK);
            return;
        }

        String finalUrl = HttpUrl
                .parse(REQUEST_PATH_JOIN_TO_CONTEST)
                .newBuilder()
                .addQueryParameter("contestManager", contestManagerName)
                .build()
                .toString();

        HttpClientUtil.runAsyncGet(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("Could not response well");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try(ResponseBody responseBody = response.body()) {
                    if (response.code() == 200) {
                        TeamDetailsContestDTO responseDetails = constants.Constants.GSON_INSTANCE.fromJson(responseBody.string(), TeamDetailsContestDTO.class);
                        System.out.printf(String.valueOf(responseDetails));
                        System.out.println(responseDetails.getContestDetails().getBattleFieldName());
                        Platform.runLater(() -> {
                            parentController.updateNewContest(responseDetails);
                            parentController.dashboardInactive();
                        });
                    } else {
                        System.out.println(
                                "Could not response well, " +response.code() +": "+ response.body().string() +",  url:" + finalUrl);
                    }
                }
            }
        });
    }
}
