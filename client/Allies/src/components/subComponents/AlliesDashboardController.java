package components.subComponents;

import com.sun.istack.internal.NotNull;
import components.ContestDetailsTableController;
import components.main.AlliesMainAppController;
import components.tables.AgentsTableController;
import contestDtos.TeamDetailsContestDTO;
import http.HttpClientUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import util.Constants;

import java.io.IOException;

import static util.Constants.REQUEST_PATH_JOIN_TO_CONTEST;


public class AlliesDashboardController {

    private AlliesMainAppController parentController;
    @FXML private ScrollPane agentsTableComponent;
    @FXML private AgentsTableController agentsTableComponentController;
    @FXML private ScrollPane contestTableComponent;
    @FXML private ContestDetailsTableController contestTableComponentController;
    @FXML private Button joinButton;

    public void setAlliesMainAppController(AlliesMainAppController alliesMainAppController) {
        this.parentController = alliesMainAppController;
    }

    public void setActive(){
        contestTableComponentController.startListRefresher(Constants.REQUEST_PATH_GET_CONTESTS);
        agentsTableComponentController.startListRefresher(constants.Constants.REQUEST_PATH_USERS_UPDATE);
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

        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("Could not response well");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() == 200) {
                    agentsTableComponentController.stopListRefresher();
                    contestTableComponentController.stopListRefresher();
                    TeamDetailsContestDTO responseDetails = constants.Constants.GSON_INSTANCE.fromJson(response.body().string(),TeamDetailsContestDTO.class);
                    parentController.updateNewContest(responseDetails);
                    System.out.println("Allies was added successfully!");
                }
                else {
                    System.out.println("Could not response well, url:" + finalUrl);
                }
            }
        });

    }
}
