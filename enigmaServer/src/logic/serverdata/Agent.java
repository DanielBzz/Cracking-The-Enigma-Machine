package logic.serverdata;

import contestDtos.ActiveAgentDTO;
import contestDtos.CandidateDataDTO;

import java.util.ArrayList;
import java.util.List;

public class Agent {

    private final String agentName;
    private int amountOfThreads;
    private int amountOfTasksInASingleTake;
    private int tasksMade;
    private String alliesName;
    private List<CandidateDataDTO> candidates;
    private int version;
    private boolean inContest;

    public Agent(String agentName) {
        this.agentName = agentName;
        candidates = new ArrayList<>();
        version = 0;
        tasksMade = 0;
    }

    public void setBasicData(int amountOfThreads, int amountOfTasksInASingleTake, String alliesName){
        this.amountOfThreads = amountOfThreads;
        this.amountOfTasksInASingleTake = amountOfTasksInASingleTake;
        this.alliesName = alliesName;
    }

    public int getAmountOfTasksInASingleTake() {
        return amountOfTasksInASingleTake;
    }

    public int getAmountOfThreads() {
        return amountOfThreads;
    }

    public int getVersion() {
        return version;
    }

    public List<CandidateDataDTO> getNewCandidates() {
        return candidates.subList(version, candidates.size());
    }

    public void updateVersion(){
        version = candidates.size();
    }
    public String getAgentName() {
        return agentName;
    }

    public void setInContest(boolean inContest) {
        this.inContest = inContest;
    }

    public boolean isInContest() {
        return inContest;
    }

    public String getAlliesName() {

        return alliesName;
    }

    public ActiveAgentDTO agentDetails(){
        return new ActiveAgentDTO(agentName,amountOfThreads,amountOfTasksInASingleTake, candidates.size(),tasksMade);
    }

}
