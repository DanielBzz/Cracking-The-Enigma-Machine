package logic.serverdata;

import exceptions.NotInDictionaryException;
import logic.DecipherLogic;
import logic.EnigmaSystemEngine;
import machineDtos.EnigmaMachineDTO;
import manager.DecryptionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class UserContest {

    private final EnigmaSystemEngine machineEngine;
    private final DecryptionManager decryptionManager;
    private final BattleField field;
    private final List<Team> competitors = new ArrayList<>();

    public UserContest(EnigmaSystemEngine machineEngine, DecryptionManager decryptionManager, BattleField field) {
        this.machineEngine = machineEngine;
        this.decryptionManager = decryptionManager;
        this.field = field;
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
}
