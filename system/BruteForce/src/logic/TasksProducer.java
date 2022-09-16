package logic;

import java.util.concurrent.BlockingQueue;

public class TasksProducer implements Runnable{

    private final AgentTaskDTO details;
    private final int taskSize;
    private final BlockingQueue<Runnable> agentTasks;
    DifficultyLevel level;
    public TasksProducer(AgentTaskDTO details, int taskSize,
                         BlockingQueue<Runnable> agentTasks, DifficultyLevel level) {
        this.details = details;
        this.taskSize = taskSize;
        this.agentTasks = agentTasks;
        this.level = level;
    }

    @Override
    public void run() {

        String ABC = details.getEngineComponentsDTO().getABC();
        int numOfRotors = details.getNumOfUsedRotors();
        double tasksAmount = Math.pow(ABC.length(),numOfRotors) ;
        int tasksMade = 0;

        while (taskSize + tasksMade <tasksAmount){
            details.setInitialPositions(DecipherLogic.createSubTask(tasksMade,taskSize,numOfRotors,ABC));
            level.initialTasks(details,agentTasks);
            tasksMade+=taskSize;
        }

        if(tasksAmount % taskSize != 0){
            details.setInitialPositions(DecipherLogic.createSubTask(tasksMade,tasksAmount % taskSize, numOfRotors,ABC));
            level.initialTasks(details,agentTasks);
        }
    }


}
