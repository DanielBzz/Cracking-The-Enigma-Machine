package logic.serverdata;

import contestDtos.ActiveAgentDTO;
import contestDtos.AgentInfoDTO;
import logic.datamanager.CandidatesManager;

public class Agent {

    private final String agentName;
    private String teamName;
    private int amountOfThreads;
    private int amountOfTasksInASingleTake;
    private int tasksMade;
    private final CandidatesManager candidates;
    private boolean inContest;

    public Agent(String agentName) {
        this.agentName = agentName;
        candidates = new CandidatesManager();
        tasksMade = 0;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setBasicData(int amountOfThreads, int amountOfTasksInASingleTake, String alliesName){
        this.amountOfThreads = amountOfThreads;
        this.amountOfTasksInASingleTake = amountOfTasksInASingleTake;
        this.teamName = alliesName;
    }

    public int getAmountOfTasksInASingleTake() {
        return amountOfTasksInASingleTake;
    }

    public void setInContest(boolean inContest) {
        this.inContest = inContest;
    }

    public boolean isInContest() {
        return inContest;
    }

    public ActiveAgentDTO agentDetails(){
        return new ActiveAgentDTO(agentName,amountOfThreads,amountOfTasksInASingleTake, candidates.getSize(),tasksMade);
    }

    public AgentInfoDTO getAgentInfo(){
        return new AgentInfoDTO(agentName, teamName,amountOfTasksInASingleTake,amountOfThreads);
    }

    public void endTasks() {

        inContest = false;
        tasksMade = 0;
    }

    public void setTeam(String team) {
        teamName = team;
    }
}
