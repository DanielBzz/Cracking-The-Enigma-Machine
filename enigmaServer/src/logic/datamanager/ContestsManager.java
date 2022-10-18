package logic.datamanager;

import contestDtos.ActivePlayerDTO;
import exceptions.NoFileLoadedException;
import logic.serverdata.Team;
import logic.serverdata.UserContest;
import machineDtos.EngineDTO;
import machineDtos.EnigmaMachineDTO;

import java.util.HashSet;
import java.util.Set;

public class ContestsManager extends DataManager<UserContest> {

    public synchronized boolean addContestForUser(String userName, UserContest detailsForUser){

        boolean battleNotLoaded = (detailsForUser != null) &&
                userNameToData.values().stream().noneMatch(
                        contest -> contest != null && detailsForUser.getContestBattleName().equals(contest.getContestBattleName()));
        boolean contestIsAdded = isUserExists(userName) && userNameToData.get(userName) == null && battleNotLoaded;

        if (contestIsAdded){
            userNameToData.put(userName,detailsForUser);
        }

        return contestIsAdded;
    }

    public void initialMachineForUser(String userName, EnigmaMachineDTO args){

        if(isContestExist(userName)){
            userNameToData.get(userName).initialCodeConfiguration(args);
        } else {
            throw new NoFileLoadedException();
        }
    }

    public String encrypt(String userName, String message){

        String decryptMessage = null;

        if(isContestExist(userName)) {
            decryptMessage = userNameToData.get(userName).encryptMessage(message);
        } else {
            throw new NoFileLoadedException();
        }

        return decryptMessage;
    }

    public boolean addCompetitorToContest(String userName, Team competitor){

        boolean isAdded = isContestExist(userName);

        if(isAdded) {
            userNameToData.get(userName).addCompetitor(competitor);
        }

        return isAdded;
    }

    public EngineDTO getUserEngineDetails(String username){

        return userNameToData.get(username).getEngineInfo();
    }

    @Override
    public Set<ActivePlayerDTO> getConnectedUsersDetails(String userName) {

        Set<ActivePlayerDTO> teamDetails = new HashSet<>();

        if(isContestExist(userName)) {

            for (Team team : userNameToData.get(userName).getCompetitors()) {

                teamDetails.add(new ActivePlayerDTO(team.getTeamName(), team.getNumOfAgents(), team.getTaskSize()));
            }
        }

        return teamDetails;
    }

    public boolean updateUserDetails(String userName, Team competitor){
        if(isUserExists(userName) && userNameToData.get(userName)!=null && userNameToData.containsKey(competitor.getTeamName())){
            UserContest userContest = (UserContest) userNameToData.get(userName);
            userContest.setTeamDetails(competitor);
            return true;
        } else {
            System.out.println("There is not such a user called: " + userName);
            return false;
        }
    }

    public void changeReadyStatus(String uBoatName, boolean status){
        if(userNameToData.containsKey(uBoatName)){
            UserContest userContest = (UserContest) userNameToData.get(uBoatName);
            if(userContest.isReady() != status){
                userContest.changeReadyStatus(status);
            } else {
                System.out.println("during battle is: " + userContest.isReady() + ", and given status is: " + status);
            }
        } else {
            System.out.println("There is not such a uBoat name: " + uBoatName);
        }
    }

    public int getAmountOfMaxAlliesInBattle(String uBoatName){
        return userNameToData.get(uBoatName).getAmountOfMaxAlliesInBattle();
    }
    public void checkIfNeedToStartContest(String uBoatName){
        boolean canStartContest = true;
        if(userNameToData.get(uBoatName).getCompetitors().size() == getAmountOfMaxAlliesInBattle(uBoatName)){
            for (Team team:userNameToData.get(uBoatName).getCompetitors()) {
                canStartContest = canStartContest && team.isReady();
            }
            if(canStartContest){
                changeReadyStatus(uBoatName, true);
            } else{
                System.out.println("Not all the teams are ready");
            }

        } else{
            System.out.println("Not all the teams were assign to the contest");
        }
    }

    public void setEncryptedMessage(String uBoatName, String encryptedMessage){
        if(userNameToData.containsKey(uBoatName) && !userNameToData.get(uBoatName).isReady()){
            userNameToData.get(uBoatName).setEncryptedMessage(encryptedMessage);
        } else {
            System.out.println(uBoatName + " does not exists or in a middle of a contest!");
        }
    }

    private Boolean isContestExist(String username){
        return isUserExists(username) && userNameToData.get(username) != null;
    }
}
