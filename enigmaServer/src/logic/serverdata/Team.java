package logic.serverdata;

import contestDtos.CandidateDataDTO;

import java.util.ArrayList;
import java.util.List;

public class Team {

    private final String teamName;
    private final int numOfAgents;
    private int taskSize;
    private String uBoatName;
    private boolean ready;
    private List<CandidateDataDTO> candidates;
    private int currentLocation;

    public Team(String teamName, int numOfAgents, int size, boolean ready) {
        this.teamName = teamName;
        this.numOfAgents = numOfAgents;
        taskSize = size;
        this.ready = ready;
        candidates = new ArrayList<>();
        currentLocation = 0;
    }

    public String getTeamName() {
        return teamName;
    }

    public int getNumOfAgents() {
        return numOfAgents;
    }

    public int getTaskSize() {
        return taskSize;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    public void setTaskSize(int taskSize) {
        this.taskSize = taskSize;
    }

    public void addCandidates(List<CandidateDataDTO> newCandidates){
        candidates.addAll(newCandidates);
    }
    public List<CandidateDataDTO> getNewCandidates(){
        List<CandidateDataDTO> newCandidates = candidates.subList(currentLocation, candidates.size());
        currentLocation = candidates.size();
        return newCandidates;
    }

    public void setuBoatName(String uBoatName) {
        this.uBoatName = uBoatName;
    }

    public String getuBoatName() {
        return uBoatName;
    }
}
