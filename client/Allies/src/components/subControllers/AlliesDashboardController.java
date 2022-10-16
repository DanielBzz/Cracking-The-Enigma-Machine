package components.subControllers;

import components.PlayerDetailsComponent;
import components.main.AlliesMainAppController;
import contestDtos.ActivePlayerDTO;
import contestDtos.ContestDetailsDTO;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;

import java.util.Map;

import static constants.Constants.AGENT_TYPE;
import static util.Constants.ACTIVE_CONTEST;


public class AlliesDashboardController {

    private AlliesMainAppController parentController;
    @FXML
    private FlowPane teamsAgentsDataArea;

    @FXML
    private FlowPane contestsDataArea;

    private Map<String, PlayerDetailsComponent> agentsDetails;
    private Map<String, ContestDetailsController> contestsDetails;
    private ContestDetailsController chosenContest;
    @FXML
    private Button readyButton;

    @FXML
    void readyButtonListener(ActionEvent event) {
        //need to enter the contest
        //use join contest servlet?
        //get all the teams servlet
        //get contest details servlet
        //update the server on a new ally in contest
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
