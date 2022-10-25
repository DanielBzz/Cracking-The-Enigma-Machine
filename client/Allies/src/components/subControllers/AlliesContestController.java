package components.subControllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import components.PlayerDetailsComponent;
import components.main.AlliesMainAppController;
import contestDtos.ActivePlayerDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import okhttp3.Response;
import util.ActivePlayerDTODeserializer;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static constants.Constants.AGENT_IN_CONTEST_TYPE;
import static constants.Constants.ALLIES_TYPE;

public class AlliesContestController {
    private AlliesMainAppController parentController;
    @FXML
    private GridPane alliesDecryptionProgressAndCandidatesComponent;
    @FXML
    private AlliesDecryptionProgressAndCandidatesController alliesDecryptionProgressAndCandidatesController;
    @FXML
    private AnchorPane contestDataArea;
    @FXML
    private FlowPane contestTeamsArea;
    @FXML
    private FlowPane activeAgentsFlowPane;
    @FXML
    private Label totalTasksLabel;
    @FXML
    private Label producedTasksLabel;
    @FXML
    private Label finishedTasksLabel;
    private Map<String, AgentInContestController> agents;

    public void setAlliesMainAppController(AlliesMainAppController alliesMainAppController) {
        this.parentController = alliesMainAppController;
    }

    public void changeContest(ContestDetailsController newController){
        contestDataArea.getChildren().clear();
        contestDataArea.getChildren().add(newController);
    }

    public void addNewTeam(PlayerDetailsComponent newTeam){
        contestTeamsArea.getChildren().add(newTeam);
    }

    public void changeTeams(List<PlayerDetailsComponent> teams){
        contestTeamsArea.getChildren().clear();
        teams.forEach(team->contestTeamsArea.getChildren().add(team));
    }

    public void clear(){
        activeAgentsFlowPane.getChildren().clear();
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

    public int getTaskSize(){
        return parentController.getTaskSize();
    }

    public void addNewAgentToScreen(ActivePlayerDTO newAgent){
        AgentInContestController newAgentController = new AgentInContestController(newAgent, AGENT_IN_CONTEST_TYPE);
        activeAgentsFlowPane.getChildren().add(newAgentController);
        agents.put(newAgent.getName(), newAgentController);
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

    public void createCompetitorsFromResponse(Response response){
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(ActivePlayerDTO.class, new ActivePlayerDTODeserializer())
                .create();
        Type listType = new TypeToken<ArrayList<ActivePlayerDTO>>(){}.getType();

        ArrayList<ActivePlayerDTO> competitors = gson.fromJson(response.body().toString(), listType);
        competitors.forEach(competitor->contestTeamsArea.getChildren().add(new PlayerDetailsComponent(competitor, ALLIES_TYPE)));
    }

    public String getContestName(){
        ContestDetailsController contestDetails = (ContestDetailsController) contestDataArea.getChildren();
        return contestDetails.getContestManagerName();
    }

}
