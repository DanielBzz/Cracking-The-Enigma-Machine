package components.subControllers;

import com.sun.istack.internal.NotNull;
import components.PlayerDetailsComponent;
import components.main.AlliesMainAppController;
import contestDtos.ActivePlayerDTO;
import contestDtos.ContestDetailsDTO;
import http.HttpClientUtil;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;

import java.io.IOException;
import java.util.Map;

import static constants.Constants.AGENT_TYPE;
import static util.Constants.ACTIVE_CONTEST;
import static util.Constants.REQUEST_PATH_JOIN_TO_CONTEST;


public class AlliesDashboardController {

    private AlliesMainAppController parentController;
    @FXML private FlowPane teamsAgentsDataArea;
    @FXML private FlowPane contestsDataArea;
    private Map<String, PlayerDetailsComponent> agentsDetails;
    private Map<String, ContestDetailsController> contestsDetails;
    private ContestDetailsController chosenContest;
    @FXML private Button readyButton;

    @FXML
    void readyButtonListener(ActionEvent event) {

        String contestManagerName = chosenContest.getContestManagerName();

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

        parentController.changeContest(chosenContest);
    }

    public void setAlliesMainAppController(AlliesMainAppController alliesMainAppController) {
        this.parentController = alliesMainAppController;
    }

    public void addNewAgent(ActivePlayerDTO agent){
        PlayerDetailsComponent newAgent = new PlayerDetailsComponent(agent, AGENT_TYPE);
        teamsAgentsDataArea.getChildren().add(newAgent);
        agentsDetails.put(agent.getName(), newAgent);
    }

    public void deleteAgent(String agentName){
        teamsAgentsDataArea.getChildren().remove(agentsDetails.get(agentName));
        agentsDetails.remove(agentName);
    }

    public void addNewContest(ContestDetailsDTO contest){
        ContestDetailsController newContest = new ContestDetailsController(contest);
        EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                readyButton.setDisable(newContest.getStatusLabel().getText().equals(ACTIVE_CONTEST));
                chosenContest = newContest;
            }
        };
        newContest.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);
        contestsDataArea.getChildren().add(newContest);
        contestsDetails.put(contest.getContestManagerName(), newContest);
    }

    public void deleteContest(String uBoatName){
        contestsDataArea.getChildren().remove(contestsDetails.get(uBoatName));
        contestsDetails.remove(uBoatName);
    }

    public int getTaskSize(){
        return chosenContest.getTaskSize();
    }

}
