package logic.serverdata;

import contestDtos.ActivePlayerDTO;
import contestDtos.CandidateDataDTO;

import java.util.ArrayList;
import java.util.List;

public class Agent {

    private final String agentName;
    private int amountOfThreads;
    private int amountOfTasksInASingleTake;
    private String alliesName;
    private List<CandidateDataDTO> candidates;
    private int version;

    public Agent(String agentName) {
        this.agentName = agentName;
        candidates = new ArrayList<>();
        version = 0;
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

    public String getAlliesName() {
        return alliesName;
    }
}
