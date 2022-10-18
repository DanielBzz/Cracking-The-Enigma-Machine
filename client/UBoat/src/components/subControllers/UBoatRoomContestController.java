package components.subControllers;

import components.CandidatesTableController;
import components.ConnectedUsersController;
import components.body.details.MachineConfigurationController;
import components.body.main.EncryptController;
import components.body.main.EncryptableByDictionary;
import components.main.UBoatMainAppController;
import contestDtos.CandidateDataDTO;
import decryptionDtos.DictionaryDTO;
import http.HttpClientUtil;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import logic.events.EncryptMessageEventListener;
import machineDtos.EngineDTO;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import util.CandidatesUpdate;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static util.Constants.REQUEST_PATH_SET_READY;

public class UBoatRoomContestController implements EncryptableByDictionary, CandidatesUpdate {

    private UBoatMainAppController parentController;
    @FXML private BorderPane machineConfigurationComponent;
    @FXML private MachineConfigurationController machineConfigurationComponentController;
    @FXML private GridPane encryptComponent;
    @FXML private EncryptController encryptComponentController;
    @FXML private Button readyButton;
    @FXML private Button logoutButton;
    @FXML private AnchorPane candidatesTableComponent;
    @FXML private CandidatesTableController candidatesTableComponentController;
    @FXML private GridPane connectedTeamsComponent;
    @FXML private ConnectedUsersController connectedTeamsComponentController;
    private DictionaryDTO dictionaryDetails;

    public void initial(){

        if(machineConfigurationComponentController != null){
            machineConfigurationComponentController.setParentController(this);
            parentController.addListenerForCodeSet(machineConfigurationComponentController);
        }
        if(encryptComponentController != null) {
            encryptComponentController.setParentController(this);
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

    public void setDictionaryDetails(DictionaryDTO dictionaryDetails) {

        this.dictionaryDetails = dictionaryDetails;
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

        candidatesTableComponentController.startListRefresher();

        String encryptedMessage = String.valueOf(encryptComponentController.getEncryptedMessage());

        String finalUrl = HttpUrl
                .parse(REQUEST_PATH_SET_READY)
                .newBuilder()
                .addQueryParameter("encryptedMessage", encryptedMessage)
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("Could not response well");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (!response.isSuccessful()) {
                    System.out.println("Could not response well, url:" + finalUrl);
                }
                //add the competitors
                System.out.println("encrypted message was updated and now the server is waiting for the teams to set ready!");
                encryptComponent.setDisable(true);//need to change after contest is finished (end of contest servlet)
            }
        });
    }


    //----------------------- override from encryptParent ------------------------------------------------
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

    @Override
    public EngineDTO getEngineDetails() {
        return parentController.getEngineDetails();
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

    //---------------------------------------------------------------------------------------------------------

    public void setIsCodeConfigurationSet(Boolean codeSet){
        machineConfigurationComponentController.getIsCodeConfigurationSetProperty().set(codeSet);
    }

    public SimpleBooleanProperty getIsConfigurationSetProperty(){
        return machineConfigurationComponentController.getIsCodeConfigurationSetProperty();
    }

    public void clearDetails(){
        //candidatesTableComponentController.clear();
        connectedTeamsComponentController.clearComponent();
        machineConfigurationComponentController.clearComponent();
        encryptComponentController.clearButtonActionListener(new ActionEvent());

    }

    @Override
    public void updateCandidates(CandidateDataDTO candidate) {
        Platform.runLater(() -> {
            //candidates.forEach(candidate->candidatesTableController.addNewCandidate(candidate));
    //       candidatesTableComponentController.addNewCandidate(candidate);
        });
    }

    public void setActive() {
        connectedTeamsComponentController.startListRefresher();
    }
}
