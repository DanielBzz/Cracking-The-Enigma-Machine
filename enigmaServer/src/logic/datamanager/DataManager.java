package logic.datamanager;

import contestDtos.ActivePlayerDTO;
import contestDtos.CandidateDataDTO;
import exceptions.ContestIsFinishedException;
import exceptions.UserNotExistException;

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

        if(isUserExists(username)) {
            userNameToData.remove(username);
        }
    }

    public synchronized Set<String> getUsersNames() {

        return Collections.unmodifiableSet(userNameToData.keySet());
    }

    public abstract Set<ActivePlayerDTO> getConnectedUsersDetails(String userName);

    public abstract List<CandidateDataDTO> getCandidates(String userName, int version) throws UserNotExistException, ContestIsFinishedException;



    public boolean isUserExists(String username) {

        return userNameToData.containsKey(username);
    }
}
