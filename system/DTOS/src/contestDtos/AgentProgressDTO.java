package contestDtos;

public class AgentProgressDTO {
    private final int tasksInQueue;
    private final int totalTakenTasks;
    private final int totalFinishedTasks;
    private final int totalAmountOfCandidates;

    public AgentProgressDTO(int tasksInQueue, int totalTakenTasks, int totalFinishedTasks, int totalAmountOfCandidates) {
        this.tasksInQueue = tasksInQueue;
        this.totalTakenTasks = totalTakenTasks;
        this.totalFinishedTasks = totalFinishedTasks;
        this.totalAmountOfCandidates = totalAmountOfCandidates;
    }

    public int getTasksInQueue() {
        return tasksInQueue;
    }

    public int getTotalAmountOfCandidates() {
        return totalAmountOfCandidates;
    }

    public int getTotalFinishedTasks() {
        return totalFinishedTasks;
    }

    public int getTotalTakenTasks() {
        return totalTakenTasks;
    }
}
