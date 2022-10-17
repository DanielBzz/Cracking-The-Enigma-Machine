package logic.serverdata;

import contestDtos.CandidateDataDTO;
import exceptions.NotInDictionaryException;
import logic.DecipherLogic;
import logic.EnigmaSystemEngine;
import machineDtos.EngineDTO;
import machineDtos.EnigmaMachineDTO;
import manager.DecryptionManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserContest {

    private final EnigmaSystemEngine machineEngine;
    private final DecryptionManager decryptionManager;
    private final BattleField field;
    private String encryptedMessage;
    private boolean ready;
    private boolean done;
    private List<CandidateDataDTO> candidates;
    private final Set<Team> competitors = new HashSet<>();

    public UserContest(EnigmaSystemEngine machineEngine, DecryptionManager decryptionManager, BattleField field) {
        this.machineEngine = machineEngine;
        this.decryptionManager = decryptionManager;
        this.field = field;
        this.ready = false;
        this.done = false;
        this.candidates = new ArrayList<>();
    }

    public EngineDTO getEngineInfo(){
        return machineEngine.displayingMachineSpecification();
    }
    public String getContestBattleName(){

        return field.getBattleName();
    }
    public void initialCodeConfiguration(EnigmaMachineDTO initialArgs) {

        if(initialArgs == null){
            machineEngine.automaticMachineInit();
        }
        else{
            machineEngine.manualMachineInit(initialArgs);
        }
    }
    public String encryptMessage(String message){

        String newMessage = DecipherLogic.excludeSpecialCharactersFromWord(message,decryptionManager.getExcludeChars());
        Set<String> wordsToEncrypt =  DecipherLogic.stringToWords(newMessage);

        for (String word: wordsToEncrypt) {
            if (!decryptionManager.getWordsDictionary().contains(word)) {
                throw new NotInDictionaryException(word);
            }
        }

        return machineEngine.encryptString(newMessage);
    }
    public void addCompetitor(Team newCompetitor){

        competitors.add(newCompetitor);
    }
    public Set<Team> getCompetitors() {
        return competitors;
    }
    public void setTeamDetails(Team team){
        competitors.removeIf(t->t.getTeamName().equals(team.getTeamName()));
        competitors.add(team);
    }
    public void changeReadyStatus(boolean status){
        if(ready != status){
            ready = status;
        }
        else {
            System.out.println("during battle is: " + ready + ", and given status is: " + status);
        }
    }
    public boolean isReady() {
        return ready;
    }
    public int getAmountOfMaxAlliesInBattle(){
        return field.getNumberOfAllies();
    }

    public void setEncryptedMessage(String encryptedMessage){
        if (!ready){
            this.encryptedMessage = encryptedMessage;
        } else {
            System.out.println("already in a middle of a contest!");
        }
    }
    public boolean isDone() {
        return done;
    }
    public void setDone(boolean done) {
        if(this.done != done){
            this.done = done;
        } else {
            System.out.println("you are trying to change: " + this.done + " to: " + done);
        }
    }
    public void addCandidatesAndCheckForFinishContest(List<CandidateDataDTO> newCandidates){
        candidates.addAll(newCandidates);
        newCandidates.forEach(candidate->{
            if(candidate.getDecryptedMessage().equals(encryptedMessage)){
                done = true;
            }
        });
    }

}
