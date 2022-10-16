package logic.datamanager;

import logic.serverdata.Team;
import logic.serverdata.UserContest;

import java.util.*;

public abstract class DataManager<T> {

    protected final Map<String,T> userNameToData = new HashMap<>();

    public synchronized boolean addUser(String username) {      // maybe change it to exception

        boolean userIsAdded = !isUserExists(username);

        if(userIsAdded) {
            userNameToData.put(username, null);
        }

        return userIsAdded;
    }

    public synchronized void removeUser(String username) {

        userNameToData.remove(username);
    }

    public synchronized Set<String> getUsersNames() {

        return Collections.unmodifiableSet(userNameToData.keySet());
    }

    public synchronized Set<Team> getUsersData(String contestManager){
        UserContest userContest = (UserContest)userNameToData.get(contestManager);
        return new HashSet<>(userContest.getCompetitors());
    }

    public boolean isUserExists(String username) {

        return userNameToData.containsKey(username);
    }

    public boolean setTeamDetails(String dataManager, Team team){
        if(userNameToData.containsKey(dataManager)){
            UserContest userContest = (UserContest) userNameToData.get(dataManager);
            userContest.setTeamDetails(team);
            return true;
        }
        else {
            System.out.println("There is not such a user called: " + dataManager);
            return false;
        }
    }
}
