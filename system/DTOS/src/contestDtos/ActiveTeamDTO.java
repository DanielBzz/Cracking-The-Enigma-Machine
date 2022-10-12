package contestDtos;

public class ActiveTeamDTO {
    private final String teamName;
    private final int amountOfAgents;
    private final int taskSize;

    public ActiveTeamDTO(String teamName, int amountOfAgents, int taskSize){
        this.teamName = teamName;
        this.amountOfAgents = amountOfAgents;
        this.taskSize = taskSize;
    }

    public String getTeamName() {
        return teamName;
    }

    public int getAmountOfAgents() {
        return amountOfAgents;
    }

    public int getTaskSize() {
        return taskSize;
    }
}
