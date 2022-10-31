package components.subComponents;

import components.ContestDetailsController;
import components.ContestDetailsTableController;
import components.DynamicComponent;
import components.main.AgentMainAppController;
import contestDtos.ContestDetailsDTO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import util.ContestPresenter;
import javafx.scene.control.Label;
import util.RefresherController;

import java.io.IOException;


public class ContestAndTeamDataController implements ContestPresenter {
    private AgentMainAppController parentController;
    @FXML
    private Label teamNameLabel;
    @FXML
    private AnchorPane contestDetailsPlace;
    private ContestDetailsController contestDetailsComponentController;

    public void initial(){
        try {
            FXMLLoader load = new FXMLLoader();
            load.setLocation(ContestDetailsController.class.getResource("contest-details.fxml"));
            Node agentComponent = load.load();
            contestDetailsPlace.getChildren().add(agentComponent);
            DynamicComponent.fitToPane(agentComponent);
            contestDetailsComponentController = load.getController();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    public void updateContestData(ContestDetailsDTO contestData){
        contestDetailsComponentController.initial(contestData);
    }

    public void setAgentMainAppController(AgentMainAppController agentMainAppController) {
        this.parentController = agentMainAppController;
    }

    public void setTeamNameLabel(String teamName) {
        this.teamNameLabel.setText(teamName);
    }

    public void clearTable(){
        contestDetailsPlace.getChildren().clear();
    }

}
