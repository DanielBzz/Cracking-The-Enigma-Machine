package manager;

import logic.*;
import machineDtos.EngineDTO;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;
import java.util.function.Consumer;

public class DecryptionManager {

    private final int numberOfAgents;
    private Set<String> wordsDictionary;
    private final String excludeChars;
    private EnigmaEngine machineEngine;
    private ThreadPoolExecutor threadPool;
    private BlockingQueue<Runnable> agentTasks;
    private DifficultyLevel level;
    private int taskSize = 1000;
    private final AgentsAnswersQueue answersQueue = new AgentsAnswersQueue();
    Consumer<AgentAnswerDTO> updateQueue;

    public DecryptionManager(int agentsNumber, Set<String> dictionary,String excludeChars ){

        numberOfAgents = agentsNumber;
        ThreadFactory threadFactory = new ThreadFactory() {
            final String name = "Agent";
            int agentNumber = 1;

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r,name + agentNumber++);
            }
        };
        wordsDictionary = dictionary;
        System.out.println(wordsDictionary);
        this.excludeChars = excludeChars;
        agentTasks = new LinkedBlockingQueue<>(1000);
        threadPool = new ThreadPoolExecutor(agentsNumber,agentsNumber,5, TimeUnit.SECONDS,agentTasks,threadFactory);
        updateQueue = answersQueue::add;
    }

    public AgentsAnswersQueue getAnswersQueue(){
        return answersQueue;
    }
    public void setMachineEngine(EnigmaEngine machineEngine) {
        this.machineEngine = machineEngine;
    }

    public int getNumberOfAgents() {

        return numberOfAgents;
    }

    public Set<String> getWordsDictionary() {

        return wordsDictionary;
    }

    public void decryptMessage(String message, List<Integer> rotorsId, String reflectorId){

        AgentTaskDTO details = new AgentTaskDTO();
        EngineDTO engineDetails = machineEngine.displayingMachineSpecification();
        details.setEngineComponentsDTO(engineDetails.getEngineComponentsInfo());
        details.setNumOfUsedRotors(engineDetails.getNumOfUsedRotors());
        details.setDictionary(wordsDictionary);
        details.setMessageToDecrypt(message);
        details.setRotorsId(rotorsId);
        details.setReflectorId(reflectorId);
        answersQueue.clear();
        details.setUpdateAnswer(updateQueue);

        threadPool.prestartAllCoreThreads();

        new Thread(new TasksProducer(details,taskSize,agentTasks,level)).start();
    }

    public void setLevel(DifficultyLevel level) {
        this.level = level;
    }

    public void shoutDown(){

        threadPool.shutdown();
        try {
            threadPool.awaitTermination(5,TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
