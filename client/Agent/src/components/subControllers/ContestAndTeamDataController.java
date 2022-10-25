package components.subControllers;

import components.ContestDetailsTableController;
import components.main.AgentMainAppController;
import javafx.fxml.FXML;
import util.ContestPresenter;

import java.awt.*;

public class ContestAndTeamDataController implements ContestPresenter {
    private AgentMainAppController parentController;
    @FXML
    private Label teamNameLabel;
    @FXML
    private ScrollPane contestDetailsComponent;
    @FXML
    private ContestDetailsTableController contestDetailsComponentController;

    public void setAgentMainAppController(AgentMainAppController agentMainAppController) {
        this.parentController = agentMainAppController;
    }

    public void setTeamNameLabel(String teamName) {
        this.teamNameLabel.setText(teamName);
    }
}
