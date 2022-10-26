package contestDtos;

public class ActiveAgentDTO extends ActivePlayerDTO{
    private final int amountOfCandidates;
    public ActiveAgentDTO(String name, int amount, int size, int amountOfCandidates) {
        super(name, amount, size);
        this.amountOfCandidates = amountOfCandidates;
    }

    public int getAmountOfCandidates() {
        return amountOfCandidates;
    }
}
