package components.subComponents;

import com.sun.istack.internal.NotNull;
import components.AgentsListController;
import components.ContestDetailsTableController;
import components.main.AlliesMainAppController;
import contestDtos.TeamDetailsContestDTO;
import http.HttpClientUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import util.Constants;

import java.io.IOException;

import static util.Constants.REQUEST_PATH_JOIN_TO_CONTEST;


public class AlliesDashboardController {

    private AlliesMainAppController parentController;
    @FXML private AnchorPane agentsPlace;
    private AgentsListController agentsTableComponentController;
    @FXML private AnchorPane contestDetailsPlace;
    private ContestDetailsTableController contestTableComponentController;
    @FXML private Button joinButton;

    public void initial(){
        try {
            FXMLLoader load = new FXMLLoader();
            load.setLocation(AgentsListController.class.getResource("agents-list.fxml"));
            agentsPlace.getChildren().add(load.load());
            agentsTableComponentController = load.getController();

            load = new FXMLLoader();
            load.setLocation(ContestDetailsTableController.class.getResource("contest-details-table.fxml"));
            contestDetailsPlace.getChildren().add(load.load());
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
        parentController.ContestInactive();
    }

    public void setInactive(){
        contestTableComponentController.stopListRefresher();
        agentsTableComponentController.stopListRefresher();
    }

    public void clearComponent(){
        agentsTableComponentController.cleanTable();
        contestTableComponentController.cleanTable();
    }

    @FXML
    void joinButtonListener(ActionEvent event) {

        String contestManagerName = contestTableComponentController.getChosenContestUserName();

        if(contestManagerName == null){
            // send message to user(pop up message)
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
                if (response.code() == 200) {
                    parentController.dashboardInactive();
                    if(response.body() != null){
                        TeamDetailsContestDTO responseDetails = constants.Constants.GSON_INSTANCE.fromJson(response.body().string(),TeamDetailsContestDTO.class);
                        parentController.updateNewContest(responseDetails);
                    }
                    System.out.println("Allies was added successfully!");
                }
                else {
                    System.out.println("Could not response well, url:" + finalUrl);
                }
                response.close();
            }
        });

    }
}
