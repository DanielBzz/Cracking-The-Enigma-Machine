package logic.datamanager;

import contestDtos.ActivePlayerDTO;
import exceptions.UserNotExistException;
import logic.serverdata.Team;

import java.util.Set;

public class TeamsManager extends DataManager<Team>{

    @Override
    public synchronized boolean addUser(String username) {      // maybe change it to exception
        boolean isAdded = super.addUser(username);
        userNameToData.put(username,new Team(username));

        return isAdded;
    }

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

    public void setUserReady(String username, int teamTaskSize) throws Exception {

        if (!isUserExists(username)){
            throw new UserNotExistException(username);
        }

        Team team = userNameToData.get(username);
        team.setTaskSize(teamTaskSize);
        team.setReady(true);
    }

    public void addAgent(String alliesName){
     //   userNameToData.get(alliesName).increaseNumOfAgents();
    }
}
