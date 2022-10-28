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

        if(!isContestExist(userName)) {
            throw new NoFileLoadedException();
        }

        return userNameToData.get(userName).encryptMessage(message);
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
                teamDetails.add(team.teamDetails());
        }

        return teamDetails;
    }

    public void addNewCompetitorToContest(String userName, Team competitor){

        if(!isContestExist(userName)) {
            throw new ContestNotExistException(userName);
        } else if (competitor.getContestName() != null) {
            throw new Error("User already in contest");
        }

        userNameToData.get(userName).addCompetitor(competitor);
        competitor.setContestName(userName);
    }

    public void setContestUserReady(String username) throws ContestNotExistException {

        if(!isContestExist(username)){
            throw new ContestNotExistException(username);
        }

        if(!userNameToData.get(username).isReady()) {
            UserContest contest = userNameToData.get(username);
            contest.setReady(true);
        }

        checkIfNeedToStartContest(username);
    }

    public void checkIfNeedToStartContest(String username){

        if(!isContestExist(username)){
            throw new ContestNotExistException(username);
        }

        UserContest contest = userNameToData.get(username);
        boolean canStartContest = isContestExist(username) &&
                contest.contestIsFull() &&
                contest.getCompetitors().stream().allMatch(Team::isReady) &&
                contest.isReady();

        if(canStartContest){
            contest.startContest();
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
            userNameToData.get(username).getCompetitors().forEach(competitor-> competitor.setContestName(null));
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

    public synchronized ContestDetailsDTO getContestDetails(String username){

        ContestDetailsDTO value = null;

        if(isContestExist(username)){
            value = userNameToData.get(username).getContestDetails();
        }

        return value;
    }
}