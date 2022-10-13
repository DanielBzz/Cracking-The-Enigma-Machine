package logic.serverdata;

public class Team {

    private final String teamName;
    private final int numOfAgents;
    private final int taskSize;

    public Team(String teamName, int numOfAgents, int size) {
        this.teamName = teamName;
        this.numOfAgents = numOfAgents;
        taskSize = size;
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
}
