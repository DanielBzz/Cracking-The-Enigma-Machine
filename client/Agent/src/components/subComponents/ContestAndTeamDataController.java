package components.subComponents;

import components.ContestDetailsTableController;
import components.DynamicComponent;
import components.main.AgentMainAppController;
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
    @FXML
    private ContestDetailsTableController contestDetailsComponentController;

    public void initial(){
        try {
            FXMLLoader load = new FXMLLoader();
            load.setLocation(ContestDetailsTableController.class.getResource("contest-details-table.fxml"));
            Node agentComponent = load.load();
            contestDetailsPlace.getChildren().add(agentComponent);
            DynamicComponent.fitToPane(agentComponent);
            contestDetailsComponentController = load.getController();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    public void setAgentMainAppController(AgentMainAppController agentMainAppController) {
        this.parentController = agentMainAppController;
    }

    public void setTeamNameLabel(String teamName) {
        this.teamNameLabel.setText(teamName);
    }

    public void clearTable(){
        contestDetailsComponentController.cleanTable();
    }

    public void setActive(){
        contestDetailsComponentController.startListRefresher(constants.Constants.REQUEST_PATH_USERS_UPDATE);
    }

}
