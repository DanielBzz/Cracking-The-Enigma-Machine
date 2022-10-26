package components;

import constants.Constants;
import contestDtos.ActivePlayerDTO;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import util.RefresherController;

import java.util.Arrays;

public class ConnectedUsersController extends RefresherController {

    @FXML private VBox activeTeamsDetailsPane;

    @Override
    public void updateList(String jsonUserList){        // in case of allie should see if some uBoat is chosen and save choose

        ActivePlayerDTO[] updatedUsers = Constants.GSON_INSTANCE.fromJson(jsonUserList, ActivePlayerDTO[].class);

        Platform.runLater(()->{
            clearComponent();
            Arrays.asList(updatedUsers).forEach(this::addNewTeamDetails);
        });
    }

    public void addNewTeamDetails(ActivePlayerDTO newTeam){

        activeTeamsDetailsPane.getChildren().add(new PlayerDetailsComponent(newTeam, Constants.ALLIES_TYPE));
    }

    public void clearComponent() {

        activeTeamsDetailsPane.getChildren().clear();
        stopListRefresher();
    }
}