package contestDtos;

public class AgentInfoDTO {
    private final String agentName;
    private final String teamName;
    private final int amountOfTasksInSingleTake;
    private final int amountOfThreads;

    public AgentInfoDTO(String agentName, String teamName, int amountOfTasksInSingleTake, int amountOfThreads) {
        this.agentName = agentName;
        this.teamName = teamName;
        this.amountOfTasksInSingleTake = amountOfTasksInSingleTake;
        this.amountOfThreads = amountOfThreads;
    }

    public String getAgentName() {
        return agentName;
    }

    public int getAmountOfThreads() {
        return amountOfThreads;
    }

    public String getTeamName() {
        return teamName;
    }

    public int getAmountOfTasksInSingleTake() {
        return amountOfTasksInSingleTake;
    }
}
