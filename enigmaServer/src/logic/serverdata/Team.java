package logic.serverdata;

import contestDtos.ActivePlayerDTO;
import contestDtos.CandidateDataDTO;
import logic.datamanager.CandidatesManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Team {

    private final String teamName;
    private int taskSize;
    private String contestName;
    private boolean ready;
    private List<Agent> teamAgents;
    private CandidatesManager candidates;

    public Team(String teamName) {
        this.teamName = teamName;
        taskSize = 0;
        this.ready = false;
        teamAgents = new ArrayList<>();
        candidates = new CandidatesManager();
    }

    public String getTeamName() {
        return teamName;
    }

    public void setContestName(String contestName) {
        this.contestName = contestName;
    }

    public String getContestName() {

        return contestName;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) throws Exception {

        if(ready && taskSize!=0 && teamAgents.size()!=0 && contestName!= null){
            this.ready = true;
        } else if (ready) {
            throw new Exception("can't initial team is ready, set correct arguments");
        }else {
            this.ready = false;
        }
    }

    public void setTaskSize(int taskSize) {
        this.taskSize = taskSize;
    }

    public void addCandidates(List<CandidateDataDTO> newCandidates){
        candidates.addNewCandidates(newCandidates);
    }

    public List<CandidateDataDTO> getNewCandidates(int lastVersion){
        return candidates.getNewCandidates(lastVersion);
    }

    public Set<ActivePlayerDTO> agentsDetails(){

        Set<ActivePlayerDTO> agentsDetails = new HashSet<>();
        teamAgents.forEach(agent -> agentsDetails.add(agent.agentDetails()));

        return agentsDetails;
    }

    public ActivePlayerDTO teamDetails(){

        return new ActivePlayerDTO(teamName, agentsDetails().size(), taskSize);
    }
}
