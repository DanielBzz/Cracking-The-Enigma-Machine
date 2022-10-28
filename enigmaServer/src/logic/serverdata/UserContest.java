package logic.serverdata;


import contestDtos.CandidateDataDTO;
import contestDtos.ContestDetailsDTO;
import decryptionDtos.DictionaryDTO;
import exceptions.ReadyForContestException;
import exceptions.NotInDictionaryException;
import javafx.util.Pair;
import logic.DecipherLogic;
import logic.EnigmaSystemEngine;
import logic.datamanager.CandidatesManager;
import machineDtos.EngineDTO;
import machineDtos.EnigmaMachineDTO;
import manager.DecryptionManager;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserContest {

    private final EnigmaSystemEngine machineEngine;
    private final DecryptionManager decryptionManager;
    private final BattleField field;
    private String encryptedMessage;
    private boolean ready;
    private boolean inContest;
    private final Set<Team> competitors = new HashSet<>();
    private CandidatesManager candidates;

    public UserContest(EnigmaSystemEngine machineEngine, DecryptionManager decryptionManager, BattleField field) {
        this.machineEngine = machineEngine;
        this.decryptionManager = decryptionManager;
        this.field = field;
        this.ready = false;
        this.inContest = false;
        this.candidates = new CandidatesManager();
    }

    public EngineDTO getEngineInfo(){
        return machineEngine.displayingMachineSpecification();
    }

    public DictionaryDTO getDictionaryInfo(){
        return new DictionaryDTO(new HashSet<>(decryptionManager.getWordsDictionary()),decryptionManager.getExcludeChars());
    }

    public boolean isReady() {
        return ready;
    }

    public boolean isInContest(){
        return inContest;
    }

    public void setReady(boolean ready){
        this.ready = ready;
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

        if(ready){
            throw new ReadyForContestException();
        }

        String newMessage = DecipherLogic.excludeSpecialCharactersFromWord(message,decryptionManager.getExcludeChars());
        Set<String> wordsToEncrypt =  DecipherLogic.stringToWords(newMessage);

        for (String word: wordsToEncrypt) {
            if (!decryptionManager.getWordsDictionary().contains(word)) {
                throw new NotInDictionaryException(word);
            }
        }

        encryptedMessage = newMessage;

        return machineEngine.encryptString(newMessage);
    }
    public void addCompetitor(Team newCompetitor){

        competitors.removeIf(t->t.getTeamName().equals(newCompetitor.getTeamName()));

        if(!contestIsFull()){
            competitors.add(newCompetitor);
        }else {
            throw new Error("Contest is full you can't join to full contest");
        }
    }

    public Set<Team> getCompetitors() {

        return competitors;
    }

    public void setEncryptedMessage(String encryptedMessage){
        if (ready){
            System.out.println("already in a middle of a contest!");
            throw new ReadyForContestException();
        }

        this.encryptedMessage = encryptedMessage;
    }

    public List<CandidateDataDTO> getNewCandidates(int lastVersion){

        return candidates.getNewCandidates(lastVersion);
    }

    public boolean contestIsFull() {
        return field.getNumberOfAllies() == competitors.size();
    }

    public ContestDetailsDTO getContestDetails(){

        ContestDetailsDTO dto = new ContestDetailsDTO(
                field.getBattleName(),
                null,
                inContest,field.getLevel().name(),
                new Pair<>(field.getNumberOfAllies(),competitors.size()),
                decryptionManager.getTaskAmount(), encryptedMessage);

        return dto;
    }

    public void startContest() {

        inContest = true;

        //should update teams and make them start create tasks;
    }
}
