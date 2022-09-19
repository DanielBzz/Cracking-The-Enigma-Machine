package agent;

import decryptionDtos.AgentAnswerDTO;
import decryptionDtos.AgentTaskDTO;
import logic.DecipherLogic;
import machine.Machine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AgentTask implements Runnable {

    static int i = 0;
    private Machine enigmaMachine;
    private final Map<String,List<Character>> decryptedMessagesCandidates = new HashMap<>();
    private AgentTaskDTO details;

    public AgentTask(Machine machine , AgentTaskDTO details) {
        this.details = details;
        enigmaMachine = machine;
    }

    public synchronized void setI(){
        i++;
    }

    @Override
    public void run() {

        long taskDuration = System.nanoTime();
        StringBuilder decryptedMessage = new StringBuilder();
        Set<String> decryptedWords;

        for (List<Character> initialPosition : details.getInitialPositions()) {

            initializeConfigurationInMachine(initialPosition);
            for (char c: details.getMessageToDecrypt().toCharArray()) {
                decryptedMessage.append(enigmaMachine.encryption(c));
            }

            decryptedWords = DecipherLogic.stringToWords(decryptedMessage.toString().toLowerCase());

            if(decryptedWords.stream().allMatch(details.getDictionary()::contains)){
                System.out.println("match");
                System.out.println(decryptedMessage.toString() + "  " + initialPosition);
                decryptedMessagesCandidates.put(decryptedMessage.toString(), initialPosition);
            }
            setI();
            decryptedMessage.delete(0,decryptedMessage.length());
        }

        taskDuration = System.nanoTime() - taskDuration;

        if(decryptedMessagesCandidates.size() != 0){
            details.getUpdateAnswer().accept(new AgentAnswerDTO(decryptedMessagesCandidates,Thread.currentThread().getName(),taskDuration ));
        }
        details.getTasksMade().update();
        System.out.println(i);
    }

    private void initializeConfigurationInMachine(List<Character> initialPosition){

        int i = 0;
        for (Character pos : initialPosition) {
            enigmaMachine.setInitPositionForRotor(i,pos);
            i++;
        }
        if(initialPosition.size()!=3){
            System.out.println(false);
        }
    }

}
