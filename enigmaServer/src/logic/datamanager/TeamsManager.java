package logic.datamanager;

import contestDtos.CandidateDataDTO;
import logic.serverdata.Team;

import java.util.List;

public class TeamsManager extends DataManager<Team>{

    public synchronized Team getTeam(String userName){
        return userNameToData.get(userName);
    }
    public synchronized String getUBoatName(String userName){
        return userNameToData.get(userName).getuBoatName();
    }

}
