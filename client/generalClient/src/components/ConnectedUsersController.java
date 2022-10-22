package components;

import constants.Constants;
import contestDtos.ActivePlayerDTO;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import util.ConnectedUserListRefresher;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ConnectedUsersController {

    @FXML private VBox activeTeamsDetailsPane;
    private Timer timer;
    private TimerTask listRefresher;

    public void startListRefresher(){

        listRefresher = new ConnectedUserListRefresher(this::updateUserList);
        timer = new Timer();
        timer.schedule(listRefresher, constants.Constants.REFRESH_RATE, constants.Constants.REFRESH_RATE);
    }

    public void stopListRefresher(){        // should activate when contest starts
        timer.cancel();
    }

    private void updateUserList(List<ActivePlayerDTO> updatedUsers){        // in case of allie should see if some uBoat is chosen and save choose

        clearComponent();
        updatedUsers.forEach(this::addNewTeamDetails);
    }

    public void addNewTeamDetails(ActivePlayerDTO newTeam){

        activeTeamsDetailsPane.getChildren().add(new PlayerDetailsComponent(newTeam, Constants.ALLIES_TYPE));
    }

    public void clearComponent() {

        activeTeamsDetailsPane.getChildren().clear();
        timer.cancel();

    }
}
