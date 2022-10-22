package logic.serverdata;


import contestDtos.CandidateDataDTO;
import decryptionDtos.DictionaryDTO;
import exceptions.NotInDictionaryException;
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

    public boolean isDone() {
        return inContest;
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

        competitors.removeIf(t->t.getTeamName().equals(newCompetitor.getTeamName()));
        competitors.add(newCompetitor);
    }

    public Set<Team> getCompetitors() {

        return competitors;
    }

    public void setEncryptedMessage(String encryptedMessage){
        if (!ready){
            this.encryptedMessage = encryptedMessage;
        } else {
            System.out.println("already in a middle of a contest!");
        }
    }

    public void addCandidatesAndCheckForFinishContest(List<CandidateDataDTO> newCandidates){
        candidates.addNewCandidates(newCandidates);
        newCandidates.forEach(candidate->{
            if(candidate.getDecryptedMessage().equals(encryptedMessage)){       // it's not mean finish, should be also same configuration
                inContest = true;
            }
        });
    }

    public List<CandidateDataDTO> getNewCandidates(int lastVersion){

        return candidates.getNewCandidates(lastVersion);
    }

    public boolean contestIsFull() {
        return field.getNumberOfAllies() == competitors.size();
    }
}
