package logic.datamanager;

import contestDtos.ActivePlayerDTO;
import contestDtos.CandidateDataDTO;
import contestDtos.ContestDetailsDTO;
import exceptions.ContestNotExistException;
import exceptions.ContestNotReadyException;
import exceptions.NoFileLoadedException;
import logic.serverdata.Team;
import logic.serverdata.UserContest;
import machineDtos.EngineDTO;
import machineDtos.EnigmaMachineDTO;

import java.util.HashSet;
import java.util.List;
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

    public EngineDTO getUserEngineDetails(String username){

        return userNameToData.get(username).getEngineInfo();
    }

    @Override
    public Set<ActivePlayerDTO> getConnectedUsersDetails(String userName) {

        Set<ActivePlayerDTO> teamDetails = new HashSet<>();

        if(isContestExist(userName)) {
            throw new ContestNotExistException(userName);
        }

        for (Team team : userNameToData.get(userName).getCompetitors()) {
                teamDetails.add(new ActivePlayerDTO(team.getTeamName(), team.getNumOfAgents(), team.getTaskSize()));
        }


        return teamDetails;
    }

    public boolean addCompetitorToContest(String userName, Team competitor){

        boolean isAdded = isContestExist(userName);

        if(isAdded) {
            userNameToData.get(userName).addCompetitor(competitor);
        }

        return isAdded;
    }
                // should combine both of them to 1 method that add first, and also in use when allie change details.
    public boolean updateUserDetails(String userName, Team competitor){
        if(isContestExist(userName) && userNameToData.containsKey(competitor.getTeamName())){
            UserContest userContest = (UserContest) userNameToData.get(userName);
            userContest.addCompetitor(competitor);
            return true;
        } else {
            System.out.println("There is not such a user called: " + userName);
            return false;
        }
    }

    public void setContestUserReady(String username, String encryptedMessage) throws ContestNotExistException {

        if(!isContestExist(username)){
            throw new ContestNotExistException(username);
        }

        if(!userNameToData.get(username).isReady()) {
            UserContest contest = userNameToData.get(username);
            contest.setReady(true);
            contest.setEncryptedMessage(encryptedMessage);
        }
    }

    public void checkIfNeedToStartContest(String username){

        boolean canStartContest = isContestExist(username) &&
                userNameToData.get(username).contestIsFull() &&
                userNameToData.get(username).getCompetitors().stream().allMatch(Team::isReady) &&
                userNameToData.get(username).isReady();

        if(canStartContest){
            // should be here some statement that start the battle for all the participants or return boolean an do it in servlet
        }else {
            System.out.println("Not all the teams were assign to the contest");
            System.out.println("Not all the teams are ready");
        }
    }

    private Boolean isContestExist(String username){
        return isUserExists(username) && userNameToData.get(username) != null;
    }

    public List<CandidateDataDTO> getCandidates(String contest, int lastVersion) throws ContestNotExistException {

        if(!isContestExist(contest)){
            throw new ContestNotExistException(contest);
        } else if (!userNameToData.get(contest).isInContest()) {
            throw new ContestNotReadyException();
        }

        return userNameToData.get(contest).getNewCandidates(lastVersion);
    }

    @Override
    public synchronized void removeUser(String username){

        if(isContestExist(username)){
            userNameToData.get(username).getCompetitors().forEach(competitor-> competitor.setuBoatName(""));
        }

        super.removeUser(username);
    }

    public synchronized Set<ContestDetailsDTO> getContestsDetails() {

        Set<ContestDetailsDTO> contestsDetails = new HashSet<>();

        for (String user: userNameToData.keySet()) {
            UserContest contest = userNameToData.get(user);
            if(contest != null){
                contestsDetails.add(contest.getContestDetails());
            }
        }

        return contestsDetails;
    }
}