package components.subControllers;

import com.sun.istack.internal.NotNull;
import components.ContestDetailsTableController;
import components.main.AlliesMainAppController;
import contestDtos.ActivePlayerDTO;
import contestDtos.ContestDetailsDTO;
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

    @FXML
    void joinButtonListener(ActionEvent event) {

        String contestManagerName = contestTableComponentController.getSelectedContest().getContestManagerName();

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
                if (!response.isSuccessful()) {
                    System.out.println("Could not response well, url:" + finalUrl);
                }
                //add the competitors
                System.out.println("Allies was added successfully!");
                parentController.createCompetitorsFromResponse(response);
            }
        });

        parentController.changeContest(contestTableComponentController.getSelectedContest());
    }

    public void setAlliesMainAppController(AlliesMainAppController alliesMainAppController) {
        this.parentController = alliesMainAppController;
    }

    public void setActive(){
        contestTableComponentController.startListRefresher(Constants.REQUEST_PATH_GET_CONTESTS);
        agentsTableComponentController.startListRefresher(constants.Constants.REQUEST_PATH_USERS_UPDATE);
    }


//    public void deleteAgent(String agentName){
//        teamsAgentsDataArea.getChildren().remove(agentsDetails.get(agentName));
//        agentsDetails.remove(agentName);
//    }

//    public void addNewContest(ContestDetailsDTO contest){
//        ContestDetailsController newContest = new ContestDetailsController(contest);
//        EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent e) {
//                joinButton.setDisable(newContest.getStatusLabel().getText().equals(ACTIVE_CONTEST));
//                //chosenContest = newContest;
//            }
//        };
//        newContest.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);
//        contestsDataArea.getChildren().add(newContest);
//        contestsDetails.put(contest.getContestManagerName(), newContest);
//    }

//    public void deleteContest(String uBoatName){
//        contestsDataArea.getChildren().remove(contestsDetails.get(uBoatName));
//        contestsDetails.remove(uBoatName);
//    }


//    public int getTaskSize(){
//        return chosenContest.getTaskSize();
//    }

}
