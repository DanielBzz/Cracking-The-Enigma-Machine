package contestDtos;

public class ActiveAgentDTO extends ActivePlayerDTO{
    private final int amountOfCandidates;
    private final int tasksMade;
    public ActiveAgentDTO(String name, int amount, int size, int amountOfCandidates, int tasksMade) {
        super(name, amount, size);
        this.amountOfCandidates = amountOfCandidates;
        this.tasksMade = tasksMade;
    }

    public int getAmountOfCandidates() {
        return amountOfCandidates;
    }

    public int getTasksMade() {
        return tasksMade;
    }
}