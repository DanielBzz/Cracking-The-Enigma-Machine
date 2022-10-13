package logic.datamanager;

import exceptions.NoFileLoadedException;
import logic.serverdata.Team;
import logic.serverdata.UserContest;
import machineDtos.EnigmaMachineDTO;

public class ContestsManager extends DataManager<UserContest> {

    public boolean addContestForUser(String userName, UserContest detailsForUser){

        boolean battleNotLoaded = userNameToData.values().stream().noneMatch(
                contest -> detailsForUser.getContestBattleName().equals(contest.getContestBattleName()));
        boolean contestIsAdded = isUserExists(userName) && userNameToData.get(userName) == null && battleNotLoaded;

        if (contestIsAdded){
            userNameToData.put(userName,detailsForUser);
        }

        return contestIsAdded;
    }

    public void initialMachineForUser(String userName, EnigmaMachineDTO args){

        if(isUserExists(userName) && userNameToData.get(userName)!=null){
            userNameToData.get(userName).initialCodeConfiguration(args);
        }else {
            throw new NoFileLoadedException();
        }
    }

    public String encrypt(String userName, String message){

        String decryptMessage = null;

        if(isUserExists(userName) && userNameToData.get(userName)!=null) {
            decryptMessage = userNameToData.get(userName).encryptMessage(message);
        }else {
            throw new NoFileLoadedException();
        }

        return decryptMessage;
    }

    public boolean addCompetitorToContest(String userName, Team competitor){

        boolean isAdded = isUserExists(userName) && userNameToData.get(userName)!=null;

        if(isAdded) {
          userNameToData.get(userName).addCompetitor(competitor);
        }

        return isAdded;
    }

}
