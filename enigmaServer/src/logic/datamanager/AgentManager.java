package logic.datamanager;

import contestDtos.ActivePlayerDTO;
import contestDtos.CandidateDataDTO;
import exceptions.UserNotExistException;
import logic.serverdata.Agent;

import java.util.List;
import java.util.Set;

public class AgentManager extends DataManager<Agent> {

    @Override
    public synchronized boolean addUser(String username){
        boolean nameOk = super.addUser(username);
        if(nameOk){
            userNameToData.put(username,new Agent(username));
        }

        return nameOk;
    }

    public synchronized Agent getAgent(String userName) throws UserNotExistException {

        if(!isUserExists(userName)){
            throw new UserNotExistException(userName);
        }
        return userNameToData.get(userName);
    }

    @Override
    public Set<ActivePlayerDTO> getConnectedUsersDetails(String userName) { // should return the agent that connected to this allie
        return null;
    }

    @Override
    public List<CandidateDataDTO> getCandidates(String userName, int version) throws UserNotExistException {
        return null;
    }


    public String getTeamName(String userName) {
        return userNameToData.get(userName).getAlliesName();
    }
}
