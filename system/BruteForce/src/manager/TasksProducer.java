package manager;

import decryptionDtos.AgentTaskDTO;
import logic.DifficultyLevel;

import java.util.ArrayList;
import java.util.List;
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
            details.setInitialPositions(createSubTask(tasksMade,taskSize,numOfRotors,ABC));
            level.initialTasks(details,agentTasks);
            tasksMade+=taskSize;
        }

        if(tasksAmount % taskSize != 0){
            details.setInitialPositions(createSubTask(tasksMade,tasksAmount % taskSize, numOfRotors,ABC));
            level.initialTasks(details,agentTasks);
        }
    }

    private List<List<Character>> createSubTask(int tasksMade, double taskSize, int numOfRotors, String ABC){

        List<List<Character>> initialPositions = new ArrayList<>() ;
        List<Character> newInitialPosition;
        int tasksMadeCopy = tasksMade;

        for (int i=0; i<taskSize;i++){

            newInitialPosition = new ArrayList<>();
            tasksMadeCopy = tasksMade;

            for(int j=0; j<numOfRotors; j++){
                newInitialPosition.add(ABC.charAt(tasksMadeCopy % ABC.length()));
                tasksMadeCopy /= ABC.length();
            }
            initialPositions.add(newInitialPosition);

            tasksMade++;
        }

        return initialPositions;
    }

}
