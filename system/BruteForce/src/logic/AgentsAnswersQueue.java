package logic;

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

        while (dataQueue.isEmpty()){
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        return dataQueue.remove();
    }

    public boolean isEmpty() {
        return dataQueue.isEmpty();
    }

    public void clear(){
        dataQueue.clear();
    }
}
