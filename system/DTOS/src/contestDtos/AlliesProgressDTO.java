package contestDtos;

public class AlliesProgressDTO {
    private final int producedTasks;
    private final int finishedTasks;

    public AlliesProgressDTO(int producedTasks, int finishedTasks) {
        this.producedTasks = producedTasks;
        this.finishedTasks = finishedTasks;
    }

    public int getFinishedTasks() {
        return finishedTasks;
    }

    public int getProducedTasks() {
        return producedTasks;
    }
}
