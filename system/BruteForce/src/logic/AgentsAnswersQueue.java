package logic;

import decryptionDtos.AgentAnswerDTO;

import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Supplier;

public class AgentsAnswersQueue implements Supplier<AgentAnswerDTO> {

    Queue<AgentAnswerDTO> dataQueue = new LinkedList<>();

    public synchronized void add(AgentAnswerDTO agentAnswer){
        dataQueue.add(agentAnswer);
        notifyAll();
    }

    @Override
    public synchronized AgentAnswerDTO get() {

        try {
            while (dataQueue.isEmpty()){
                wait();
            }
        } catch (InterruptedException e) {
                throw new RuntimeException(e);
        }
            // not good need to fix it
        return dataQueue.remove();
    }

    public boolean isEmpty() {
        return dataQueue.isEmpty();
    }

    public void clear(){
        dataQueue.clear();
    }
}
