package logic.datamanager;

import contestDtos.ActivePlayerDTO;
import logic.serverdata.Team;

import java.util.Set;

public class TeamsManager extends DataManager<Team>{

    public synchronized Team getTeam(String userName){

        return userNameToData.get(userName);
    }

    public synchronized String getContestName(String userName){
        return userNameToData.get(userName).getContestName();
    }

    @Override
    public Set<ActivePlayerDTO> getConnectedUsersDetails(String userName) {
        return userNameToData.get(userName).agentsDetails();
    }

    public void addAgent(String alliesName){
        userNameToData.get(alliesName).increaseNumOfAgents();
    }
}
