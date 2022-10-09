package logic;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ContestsManager {
    private final Map<String, ContestDetails> userToContestMap = new HashMap<>();

    public synchronized boolean addUser(String username) {

        boolean userIsAdded = !isUserExists(username);

        if(userIsAdded) {
            userToContestMap.put(username, null);
        }

        return userIsAdded;
    }

    public synchronized void removeUser(String username) {

        userToContestMap.remove(username);
    }

    public synchronized Map<String,Object> getUsers() {

        return Collections.unmodifiableMap(userToContestMap);
    }

    public boolean isUserExists(String username) {

        return userToContestMap.containsKey(username);
    }

    public synchronized boolean addContestForUser(String userName, ContestDetails detailsForUser){

        boolean contestIsAdded = isUserExists(userName) && userToContestMap.get(userName) == null;

        if (contestIsAdded){
            userToContestMap.put(userName,detailsForUser);
        }

        return contestIsAdded;
    }
}
