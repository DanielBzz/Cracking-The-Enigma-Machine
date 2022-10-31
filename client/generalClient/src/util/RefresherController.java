package util;

import java.util.Timer;
import java.util.TimerTask;

public abstract class RefresherController {
    private Timer timer;
    private TimerTask listRefresher;

    public void startListRefresher(String requestUrl){

        listRefresher = new UsersListRefresher(this::updateList, requestUrl);
        timer = new Timer();
        timer.schedule(listRefresher, constants.Constants.REFRESH_RATE, constants.Constants.REFRESH_RATE);
    }

    public void stopListRefresher(){        // should activate when contest starts
       if(timer != null){
     //      timer.cancel();
        }
    }

    public abstract void updateList(String jsonUserList);

}
