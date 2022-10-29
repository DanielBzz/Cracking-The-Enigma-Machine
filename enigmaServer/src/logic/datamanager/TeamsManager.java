package logic.datamanager;

import agent.AgentTask;
import contestDtos.ActivePlayerDTO;
import contestDtos.CandidateDataDTO;
import exceptions.ContestNotReadyException;
import exceptions.UserNotExistException;
import logic.serverdata.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class TeamsManager extends DataManager<Team>{

    @Override
    public synchronized boolean addUser(String username) {      // maybe change it to exception
        boolean isAdded = super.addUser(username);
        if(isAdded){
            userNameToData.put(username,new Team(username));
        }

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

    @Override
    public List<CandidateDataDTO> getCandidates(String userName, int version) throws UserNotExistException {
        Team team;
        synchronized (userNameToData){
            if(!isUserExists(userName)){
                throw new UserNotExistException(userName);
            }
            team = userNameToData.get(userName);
        }

        if(!team.isInContest()) {
            throw new ContestNotReadyException();
        }

        return team.getNewCandidates(version);
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

    public List<AgentTask> getTasks(String username, int amountOfTasks) throws UserNotExistException, InterruptedException {

        List<AgentTask> tasks = new ArrayList<>();
        Team team;
        synchronized(userNameToData) {
            if (!isUserExists(username)) {
                throw new UserNotExistException(username);
            }
            team = userNameToData.get(username);
        }

        synchronized (team) {
            if (team.isInContest()) {
                throw new ContestNotReadyException();
            }
        }

         for(int i=0;i<amountOfTasks;i++){
             tasks.add((AgentTask)team.getTask());
         }

         return tasks;
    }

    public void addCandidates(String teamName, List<CandidateDataDTO> newCandidates) throws UserNotExistException {
        Team team;
        synchronized (userNameToData){
            if(!isUserExists(teamName)){
                throw new UserNotExistException(teamName);
            }
            team = userNameToData.get(teamName);
        }

        if(!team.isInContest()) {
            throw new ContestNotReadyException();
        }

        team.addCandidates(newCandidates);
    }
}
