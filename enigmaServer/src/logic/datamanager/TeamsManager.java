package logic.datamanager;

import logic.serverdata.Team;

public class TeamsManager extends DataManager<Team>{

    public synchronized Team getTeam(String userName){
        return userNameToData.get(userName);
    }

}
