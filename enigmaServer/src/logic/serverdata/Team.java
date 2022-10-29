package logic.serverdata;

import contestDtos.ActivePlayerDTO;
import contestDtos.CandidateDataDTO;
import decryptionDtos.AgentTaskDTO;
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
    private String contestName;
    private boolean ready;
    private boolean inContest;
    private List<Agent> teamAgents;
    private CandidatesManager candidates;
    private BlockingQueue<Runnable> taskQueue = new LinkedBlockingQueue<>(DecipherLogic.MAXIMUM_TASKS);
    private Consumer<List<CandidateDataDTO>> contestManagerConsumer;

    public Team(String teamName) {
        this.teamName = teamName;
        taskSize = 0;
        this.ready = false;
        teamAgents = new ArrayList<>();
        candidates = new CandidatesManager();
    }

    public String getTeamName() {
        return teamName;
    }

    public void setContestName(String contestName) {
        this.contestName = contestName;
    }

    public String getContestName() {

        return contestName;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) throws Exception {

        if(ready && taskSize!=0 && teamAgents.size()!=0 && contestName!= null){
            this.ready = true;
        } else if (ready) {
            throw new Exception("can't initial team is ready, set correct arguments");
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

    public List<CandidateDataDTO> getNewCandidates(int lastVersion){
        return candidates.getNewCandidates(lastVersion);
    }

    public Set<ActivePlayerDTO> agentsDetails(){

        Set<ActivePlayerDTO> agentsDetails = new HashSet<>();
        teamAgents.forEach(agent -> agentsDetails.add(agent.agentDetails()));

        return agentsDetails;
    }

    public ActivePlayerDTO teamDetails(){

        return new ActivePlayerDTO(teamName, agentsDetails().size(), taskSize);
    }

    public void startCompeting(EnigmaSystemEngine machineEngine, DecryptionManager decryptionManager, String encryptedMessage, DifficultyLevel level, Consumer<List<CandidateDataDTO>> listConsumer){

        inContest = true;
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

        new Thread(new TasksProducer(details,taskSize,taskQueue,level)).start();

        teamAgents.forEach(agent -> agent.setInContest(true));
    }

    public Runnable getTask() throws InterruptedException {
        return taskQueue.take();
    }
}
