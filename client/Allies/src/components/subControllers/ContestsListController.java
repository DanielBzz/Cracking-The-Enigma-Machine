package components.subControllers;

import constants.Constants;
import contestDtos.ContestDetailsDTO;
import javafx.application.Platform;
import util.UsersListRefresher;

import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

public class ContestsListController {

    private Timer timer;
    private TimerTask listRefresher;

    public void startListRefresher(){

        listRefresher = new UsersListRefresher(this::updateUserList, Constants.REQUEST_PATH_GET_CONTESTS);
        timer = new Timer();
        timer.schedule(listRefresher, constants.Constants.REFRESH_RATE, constants.Constants.REFRESH_RATE);
    }

    public void stopListRefresher(){        // should activate when contest starts
        timer.cancel();
    }

    private void updateUserList(String jsonUserList){        // in case of allie should see if some uBoat is chosen and save choose

        ContestDetailsDTO[] updatedUsers = Constants.GSON_INSTANCE.fromJson(jsonUserList, ContestDetailsDTO[].class);

        Platform.runLater(()->{

            // should check if there is chosen contest before refresher

            clearComponent();
            Arrays.asList(updatedUsers).forEach(this::addContest);
        });
    }

    public void addContest(ContestDetailsDTO newTeam){

        //
    }

    public void clearComponent() {

        // clear the page here
        timer.cancel();
    }
}
