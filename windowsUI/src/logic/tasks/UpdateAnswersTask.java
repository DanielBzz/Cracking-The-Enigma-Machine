package logic.tasks;

import decryptionDtos.AgentAnswerDTO;
import javafx.application.Platform;
import javafx.concurrent.Task;
import logic.TasksMadeData;

import java.util.concurrent.BlockingQueue;
import java.util.function.Consumer;

public class UpdateAnswersTask extends Task<Boolean> {

    private int amountOfTasks;
    private TasksMadeData tasksMade;
    private BlockingQueue<AgentAnswerDTO> answersQueue;
    private Consumer<AgentAnswerDTO> answersConsumer;

    public UpdateAnswersTask(int amountOfTasks, TasksMadeData tasksMade, BlockingQueue<AgentAnswerDTO> answersQueue, Consumer<AgentAnswerDTO> answersConsumer) {
        this.amountOfTasks = amountOfTasks;
        this.tasksMade = tasksMade;
        this.answersQueue = answersQueue;
        this.answersConsumer = answersConsumer;
    }

    @Override
    protected Boolean call() throws Exception {

        while(tasksMade.get() < amountOfTasks || !answersQueue.isEmpty()){

            AgentAnswerDTO answer = answersQueue.take();
            Platform.runLater(()-> answersConsumer.accept(answer));
        }

        return false;
    }
}
