package logic.serverdata;

import contestDtos.ActivePlayerDTO;
import contestDtos.CandidateDataDTO;
import decryptionDtos.AgentTaskDTO;
import exceptions.ContestIsFinishedException;
import exceptions.ContestNotExistException;
import logic.*;
import logic.datamanager.CandidatesManager;
import machineDtos.EngineDTO;
import manager.DecryptionManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

public class Team {

    private final String teamName;
    private int taskSize;
    private String contestManagerName;
    private boolean ready;
    private boolean inContest;
    private final List<Agent> teamAgents = new ArrayList<>();
    private final CandidatesManager candidates = new CandidatesManager();
    private final BlockingQueue<Runnable> taskQueue = new LinkedBlockingQueue<>(DecipherLogic.MAXIMUM_TASKS);
    private Consumer<List<CandidateDataDTO>> contestManagerConsumer;
    private Thread taskProducerThread;
    private CandidateDataDTO winnerCandidate;

    public Team(String teamName) {
        this.teamName = teamName;
        taskSize = 0;
        this.ready = false;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setContestManagerName(String contestManagerName) {
        this.contestManagerName = contestManagerName;
    }

    public String getContestManagerName() {

        return contestManagerName;
    }

    public synchronized void addAgentToTeam(Agent agent){
        //Agent newAgent = new Agent(agent.getName());
        //newAgent.setBasicData(agent.getAmount(), agent.getSize(), teamName);
        teamAgents.add(agent);
        System.out.println("in Team- addAgentToTeam: " + agent);
    }
    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) throws Exception {

        if(ready && taskSize!=0 && teamAgents.size()!=0 && contestManagerName!= null){
            this.ready = true;
        } else if (ready) {
            throw new Exception("can't initial team is ready,your task size/agents not initial yet");
        }else {
            this.ready = false;
        }
    }

    public void setTaskSize(int taskSize) {
        this.taskSize = taskSize;
    }

    public void setInContest(boolean inContest) {
        this.inContest = inContest;
    }

    public boolean isInContest() {
        return inContest;
    }

    public void addCandidates(List<CandidateDataDTO> newCandidates){
        candidates.addNewCandidates(newCandidates);

        List<CandidateDataDTO> candidateForContestManager = new ArrayList<>();
        for(CandidateDataDTO candidate:newCandidates){
            candidateForContestManager.add(new CandidateDataDTO(candidate.getDecryptedMessage(),teamName, candidate.getConfiguration()));
        }

        contestManagerConsumer.accept(candidateForContestManager);
    }

    public List<CandidateDataDTO> getNewCandidates(int lastVersion) throws ContestIsFinishedException {

        if(winnerCandidate != null){
            throw new ContestIsFinishedException(winnerCandidate);
        }

        return candidates.getNewCandidates(lastVersion);
    }

    public Set<ActivePlayerDTO> agentsDetails(){

        Set<ActivePlayerDTO> agentsDetails = new HashSet<>();
        teamAgents.forEach(agent -> agentsDetails.add(agent.agentDetails()));

        return agentsDetails;
    }

    public synchronized ActivePlayerDTO getTeamDetails(){

        return new ActivePlayerDTO(teamName, agentsDetails().size(), taskSize);
    }

    public void startCompeting(EnigmaSystemEngine machineEngine, DecryptionManager decryptionManager, String encryptedMessage, DifficultyLevel level, Consumer<List<CandidateDataDTO>> listConsumer){

        inContest = true;
        ready = false;
        AgentTaskDTO details = new AgentTaskDTO();
        candidates.clear();
        contestManagerConsumer = listConsumer;

        EngineDTO engineDetails = machineEngine.displayingMachineSpecification();
        details.setNumOfUsedRotors(engineDetails.getNumOfUsedRotors());
        details.setDictionary(decryptionManager.getWordsDictionary());
        details.setMessageToDecrypt(encryptedMessage);
        details.setRotorsId(engineDetails.getMachineCurrentInfo().getRotorsID());
        details.setReflectorId(engineDetails.getMachineCurrentInfo().getReflectorID());
        details.setTasksMade(new Counter());
        if(machineEngine instanceof EnigmaEngine) {
            details.setEngineComponentsDTO(((EnigmaEngine)machineEngine).getEngineComponentsDto());
        }

        taskProducerThread = new Thread(new TasksProducer(details,taskSize,taskQueue,level));
        taskProducerThread.start();

        System.out.println("in Team, startCompeting, agents are: " + teamAgents);
        teamAgents.forEach(agent -> agent.setInContest(true));
    }

    public void endCompeting(CandidateDataDTO winner) {

        if(!inContest){
            throw new ContestNotExistException(teamName);
        }

        System.out.println("================================= finish competing for allie ============================================");
        winnerCandidate = winner;
        taskProducerThread.interrupt();
        inContest = false;
        contestManagerName = null;
        taskSize = 0;
        teamAgents.forEach(Agent::endTasks);
        teamAgents.clear();
        synchronized (taskQueue){
            taskQueue.clear();
        }
    }

    public Runnable getTask() throws InterruptedException {
        return taskQueue.take();
    }

    public void removeTeam() {
        teamAgents.forEach(agent -> agent.setTeam(null));
    }

    public synchronized void removeAgent(Agent agent) {

        teamAgents.remove(agent);
    }
}
