package logic.tasks;

import decryptionDtos.AgentAnswerDTO;
import decryptionDtos.DecryptionArgumentsDTO;
import javafx.application.Platform;
import javafx.concurrent.Task;
import logic.TasksMade;
import manager.DecryptionManager;

import java.util.function.Consumer;

public class DecryptMessageTask extends Task<Boolean> {

    DecryptionManager decryptionManager;
    DecryptionArgumentsDTO args;
    Consumer<AgentAnswerDTO> answersConsumer;

    private TasksMade tasksMade = new TasksMade();
    public DecryptMessageTask(DecryptionManager decryptionManager, DecryptionArgumentsDTO args, Consumer<AgentAnswerDTO> answersConsumer){

        this.answersConsumer = answersConsumer;
        this.decryptionManager = decryptionManager;
        this.args = args;
    }


    @Override
    protected Boolean call() throws Exception {

        tasksMade.put(0);
        decryptionManager.decryptMessage(
                args.getTaskSize(),args.getLevel(),args.getMessageToDecrypt(),args.getRotorsId(),args.getReflectorId(),tasksMade);

        while(tasksMade.get() != args.getAmountOfTasks() && !decryptionManager.getAnswersQueue().isEmpty() ){

            updateProgress(tasksMade.get(), args.getAmountOfTasks());

            AgentAnswerDTO answer = decryptionManager.getAnswersQueue().get();
            Platform.runLater(()-> answersConsumer.accept(answer));
        }

        decryptionManager.shoutDown();

        return true;
    }
}
