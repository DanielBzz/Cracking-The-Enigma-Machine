package logic;

import components.body.details.DecryptionManagerController;
import logic.tasks.DecryptMessageTask;
import decryptionDtos.AgentAnswerDTO;
import decryptionDtos.DecryptionArgumentsDTO;
import manager.DecryptionManager;

import java.util.function.Consumer;

public class DecryptLogicUI {

    private DecryptMessageTask currentTask;
    private DecryptionManagerController controllerToUpdate;
    private DecryptionManager decryptionManager;

    public void setDecryptionManager(DecryptionManager decryptionManager) {
        this.decryptionManager = decryptionManager;
    }

    public void setControllerToUpdate(DecryptionManagerController controllerToUpdate) {
        this.controllerToUpdate = controllerToUpdate;
    }

    public void setCurrentTask(DecryptMessageTask currentTask) {
        this.currentTask = currentTask;
    }

    public void decryptMessage(Consumer<AgentAnswerDTO> updateCandidates, DecryptionArgumentsDTO args){

        //decryptionManager.decryptMessage(taskSize,difficultyLevel,encryptedMessage,);

        currentTask = new DecryptMessageTask(decryptionManager,args, updateCandidates);
        controllerToUpdate.bindTaskToController(currentTask);

        new Thread(currentTask).start();
    }

    public void cancelTask(){
        currentTask.cancel();
    }

    public int getMaxAgentTask(){
        return decryptionManager.getNumberOfAgents();
    }

    public double getTaskSize(){
        return decryptionManager.getTaskAmount();
    }
}
