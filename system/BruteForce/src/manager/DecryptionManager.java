package manager;

import logic.*;
import decryptionDtos.AgentAnswerDTO;
import decryptionDtos.AgentTaskDTO;
import machineDtos.EngineDTO;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;
import java.util.function.Consumer;

public class DecryptionManager {

    private static int maxNumberOfAgents;
    private static Set<String> wordsDictionary;
    private static String excludeChars;
    private EnigmaSystemEngine machineEngine;
    private ThreadPoolExecutor threadPool;
    private BlockingQueue<Runnable> agentTasks;
    private final AgentsAnswersQueue answersQueue = new AgentsAnswersQueue();
    Consumer<AgentAnswerDTO> updateQueue;

    public DecryptionManager(EnigmaSystemEngine engine){

        ThreadFactory threadFactory = new ThreadFactory() {
            final String name = "Agent";
            int agentNumber = 1;

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r,name + agentNumber++);
            }
        };
        agentTasks = new LinkedBlockingQueue<>(1000);
        threadPool = new ThreadPoolExecutor(maxNumberOfAgents, maxNumberOfAgents,5, TimeUnit.SECONDS,agentTasks,threadFactory);
        updateQueue = answersQueue::add;
        machineEngine = engine;
    }

    public static void setMaxNumberOfAgents(int maxNumberOfAgents) {
        DecryptionManager.maxNumberOfAgents = maxNumberOfAgents;
    }

    public static void setWordsDictionary(Set<String> wordsDictionary) {
        DecryptionManager.wordsDictionary = wordsDictionary;
    }

    public static void setExcludeChars(String excludeChars) {
        DecryptionManager.excludeChars = excludeChars;
    }


    public AgentsAnswersQueue getAnswersQueue(){
        return answersQueue;
    }
    public void setMachineEngine(EnigmaEngine machineEngine) {
        this.machineEngine = machineEngine;
    }

    public int getNumberOfAgents() {

        return maxNumberOfAgents;
    }

    public Set<String> getWordsDictionary() {

        return wordsDictionary;
    }

    public void decryptMessage(int taskSize, DifficultyLevel level,String message, List<Integer> rotorsId, String reflectorId){

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

        new TasksProducer(details,taskSize,agentTasks,level).run();
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
