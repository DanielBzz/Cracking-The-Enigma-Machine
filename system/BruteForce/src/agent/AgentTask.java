package agent;

import decryptionDtos.AgentAnswerDTO;
import logic.DecipherLogic;
import machine.Machine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public class AgentTask implements Runnable {

    private final Machine enigmaMachine;
    private final List<List<Character>> initialPositions;
    private final String messageToDecrypt;
    private final Map<String,List<Character>> decryptedMessagesCandidates = new HashMap<>();
    private final Set<String> wordsDictionary;
    private final Consumer<AgentAnswerDTO> update;

    public AgentTask(Machine enigmaMachine, List<List<Character>> initialPositions,String message, Set<String> dictionary,Consumer<AgentAnswerDTO> update ){

        this.enigmaMachine = enigmaMachine;
        this.initialPositions = initialPositions;
        messageToDecrypt = message;
        wordsDictionary = dictionary;
        this.update = update;
    }

    @Override
    public void run() {
        System.out.println("in agent task");

        long taskDuration = System.nanoTime();
        StringBuilder decryptedMessage = new StringBuilder();
        Set<String> decryptedWords;

        for (List<Character> initialPosition : initialPositions) {

            initializeConfigurationInMachine(initialPosition);
            for (char c: messageToDecrypt.toCharArray()) {
                decryptedMessage.append(enigmaMachine.encryption(c));
            }

            decryptedWords = DecipherLogic.stringToWords(decryptedMessage.toString().toLowerCase());
            if(decryptedWords.stream().allMatch(wordsDictionary::contains)){
                decryptedMessagesCandidates.put(decryptedMessage.toString(), initialPosition);
                System.out.println("word match");
            }
        }

        taskDuration = System.nanoTime() - taskDuration;

        if(decryptedMessagesCandidates.size() != 0){
            update.accept(new AgentAnswerDTO(decryptedMessagesCandidates,Thread.currentThread().getName(),taskDuration ));
        }
    }

    private void initializeConfigurationInMachine(List<Character> initialPosition){

        int i = 0;
        for (Character pos : initialPosition) {
            enigmaMachine.setInitPositionForRotor(i,pos);
            i++;
        }
    }

}
