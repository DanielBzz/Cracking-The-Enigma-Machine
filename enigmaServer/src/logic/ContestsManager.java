package logic;

import exceptions.NoFileLoadedException;
import machineDtos.EnigmaMachineDTO;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ContestsManager {
    private final Map<String, UserContest> userToContestMap = new HashMap<>();

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

    public synchronized boolean addContestForUser(String userName, UserContest detailsForUser){

        boolean battleNotLoaded = userToContestMap.values().stream().noneMatch(
                contest -> detailsForUser.getContestBattleName().equals(contest.getContestBattleName()));
        boolean contestIsAdded = isUserExists(userName) && userToContestMap.get(userName) == null && battleNotLoaded;

        if (contestIsAdded){
            userToContestMap.put(userName,detailsForUser);
        }

        return contestIsAdded;
    }

    public void initialMachineForUser(String userName, EnigmaMachineDTO args){

        if(isUserExists(userName) && userToContestMap.get(userName)!=null){
            userToContestMap.get(userName).initialCodeConfiguration(args);
        }else {
            throw new NoFileLoadedException();
        }
    }

    public String encrypt(String userName, String message){

        String decryptMessage = null;

        if(isUserExists(userName) && userToContestMap.get(userName)!=null) {
            decryptMessage = userToContestMap.get(userName).encryptMessage(message);
        }else {
            throw new NoFileLoadedException();
        }

        return decryptMessage;
    }
}
