package components.subControllers;

import components.PlayerDetailsComponent;
import components.body.details.MachineConfigurationController;
import components.body.main.EncryptController;
import components.body.main.EncryptableByDictionary;
import components.main.UBoatMainAppController;
import contestDtos.ActivePlayerDTO;
import contestDtos.CandidateDataDTO;
import decryptionDtos.DictionaryDTO;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import logic.events.EncryptMessageEventListener;
import machineDtos.EngineDTO;
import util.CandidatesRefresher;
import util.CandidatesUpdate;

import java.util.Arrays;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

public class UBoatRoomContestController implements EncryptableByDictionary, CandidatesUpdate {

    private UBoatMainAppController parentController;
    private Timer timer;
    private TimerTask listRefresher;
    private BooleanProperty autoUpdate;
    @FXML private BorderPane machineConfigurationComponent;
    @FXML private MachineConfigurationController machineConfigurationComponentController;
    @FXML private GridPane encryptComponent;
    @FXML private EncryptController encryptComponentController;
    @FXML private FlowPane activeTeamsDetailsFlowPane;
    @FXML private Button readyButton;
    @FXML private Button logoutButton;
   // @FXML private AnchorPane candidatesTableComponent;
   // @FXML private CandidatesTableController candidatesTableComponentController;
    private DictionaryDTO dictionaryDetails;


    public void initial(){

        if(machineConfigurationComponentController != null){
            machineConfigurationComponentController.setParentController(this);
            parentController.addListenerForCodeSet(machineConfigurationComponentController);
        }
        if(encryptComponentController != null) {
            encryptComponentController.setParentController(this);
//            machineConfigurationComponentController.getIsCodeConfigurationSetProperty().addListener(
//                    observable -> encryptComponentController.createKeyboards(parentController.getEngineDetails().getEngineComponentsInfo().getABC()));
            encryptComponentController.setAutoStateOnly();
            initEncryptResetButtonActionListener();
            initEncryptListener();
            parentController.getEncryptedMessageProperty().addListener(
                    (observable, oldValue, newValue) ->  encryptComponentController.setEncryptedMessageLabel(newValue));
        }
    }

    public void setParentController(UBoatMainAppController uBoatRoomController) {
        this.parentController = uBoatRoomController;
    }

    @FXML
    void logoutButtonListener(ActionEvent event) {
        //delete from session

        //delete contest from all allies dashboard

        //return to login page
        parentController.close();
    }

    @FXML
    void readyButtonListener(ActionEvent event) {

        startListRefresher();
        //fire contest

    }

    public void startListRefresher() {
        listRefresher = new CandidatesRefresher(
                "http://localhost:8080/enigmaServer/contestManager",
                this::updateCandidates,
                autoUpdate);
        timer = new Timer();
        timer.schedule(listRefresher, 2000, 2000);
    }

    @Override
    public void initEncryptResetButtonActionListener(){

        encryptComponentController.getResetButtonActionProperty().addListener(
                observable -> parentController.initialMachineConfiguration(parentController.getEngineDetails().getMachineInitialInfo()));
    }

    public void initEncryptListener(){

        encryptComponentController.activateEncryptEventHandler.addListener(new EncryptMessageEventListener() {
            @Override
            public void invoke(String message) {
                parentController.encryptMessage(message);
            }
        });
    }

    public void addNewTeamDetails(ActivePlayerDTO newTeam){
        activeTeamsDetailsFlowPane.getChildren().add(new PlayerDetailsComponent(newTeam, "allies"));
    }

    public void setIsCodeConfigurationSet(Boolean codeSet){
        machineConfigurationComponentController.getIsCodeConfigurationSetProperty().set(codeSet);
    }

    public SimpleBooleanProperty getIsConfigurationSetProperty(){
        return machineConfigurationComponentController.getIsCodeConfigurationSetProperty();
    }

    public void clearDetails(){
        activeTeamsDetailsFlowPane.getChildren().clear();
        //candidatesTableComponentController.clear();
        machineConfigurationComponentController.clearComponent();
        encryptComponentController.clearButtonActionListener(new ActionEvent());
        encryptComponentController.removeOldAbcFromKeyboards();
    }

    @Override
    public EngineDTO getEngineDetails() {
        return parentController.getEngineDetails();
    }

    @Override
    public void updateCandidates(CandidateDataDTO candidate) {
        Platform.runLater(() -> {
            //candidates.forEach(candidate->candidatesTableController.addNewCandidate(candidate));
    //       candidatesTableComponentController.addNewCandidate(candidate);
        });
    }

    @Override
    public Boolean checkWordsInTheDictionary(String message) {

        Set<String> words = Arrays.stream(message.split(" ")).collect(Collectors.toSet());

        return dictionaryDetails.getWordsDictionary().containsAll(words);
    }

    @Override
    public String getStringWithoutSpecialChars(String word) {

        StringBuilder tempWord = new StringBuilder(word);
        int index;

        for (Character c: dictionaryDetails.getExcludeChars().toCharArray()) {

            while ((index = tempWord.indexOf(c.toString())) != -1){
                tempWord.deleteCharAt(index);
            }
        }

        return tempWord.toString();
    }

    public void setDictionaryDetails(DictionaryDTO dictionaryDetails) {
        this.dictionaryDetails = dictionaryDetails;
    }
}
