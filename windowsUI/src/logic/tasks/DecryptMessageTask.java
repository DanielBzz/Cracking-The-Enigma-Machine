package logic.tasks;

import decryptionDtos.AgentAnswerDTO;
import decryptionDtos.DecryptionArgumentsDTO;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.concurrent.Task;
import manager.DecryptionManager;

import java.util.function.Consumer;

public class DecryptMessageTask extends Task<Boolean> {

    DecryptionManager decryptionManager;
    DecryptionArgumentsDTO args;
    Consumer<AgentAnswerDTO> answersConsumer;
    IntegerProperty tasksMade = new SimpleIntegerProperty(0);

    public DecryptMessageTask(DecryptionManager decryptionManager, DecryptionArgumentsDTO args, Consumer<AgentAnswerDTO> answersConsumer){

        this.answersConsumer = answersConsumer;
        this.decryptionManager = decryptionManager;
        this.args = args;
    }


    @Override
    protected Boolean call() throws Exception {


        decryptionManager.decryptMessage(args.getTaskSize(),args.getLevel(),args.getMessageToDecrypt(),args.getRotorsId(),args.getReflectorId());

        //while(tasksMade.getValue() != )

        return true;
    }
}
