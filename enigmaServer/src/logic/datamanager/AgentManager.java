package logic.datamanager;

import contestDtos.ActivePlayerDTO;
import logic.serverdata.Agent;
import logic.serverdata.Team;

import java.util.Set;

public class AgentManager extends DataManager<Agent> {

    public synchronized Agent getAgent(String userName){

        return userNameToData.get(userName);
    }
    public synchronized String getAlliesName(String userName){
        return userNameToData.get(userName).getAlliesName();
    }

    @Override
    public Set<ActivePlayerDTO> getConnectedUsersDetails(String userName) { // should return the agent that connected to this allie
        return null;
    }
}
