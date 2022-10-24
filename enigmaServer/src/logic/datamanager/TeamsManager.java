package logic.datamanager;

import contestDtos.ActivePlayerDTO;
import logic.serverdata.Team;

import java.util.Set;

public class TeamsManager extends DataManager<Team>{

    public synchronized Team getTeam(String userName){

        return userNameToData.get(userName);
    }
    public synchronized String getUBoatName(String userName){
        return userNameToData.get(userName).getuBoatName();
    }

    @Override
    public Set<ActivePlayerDTO> getConnectedUsersDetails(String userName) { // should return the agent that connected to this allie
        return null;
    }
}
