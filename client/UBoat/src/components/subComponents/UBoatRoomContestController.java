package components.subComponents;

import components.CandidatesTableController;
import components.ConnectedUsersController;
import components.DynamicComponent;
import components.body.details.MachineConfigurationController;
import components.body.main.EncryptController;
import components.body.main.EncryptableByDictionary;
import components.main.UBoatMainAppController;
import contestDtos.CandidateDataDTO;
import decryptionDtos.DictionaryDTO;
import http.HttpClientUtil;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import logic.events.EncryptMessageEventListener;
import machineDtos.EngineDTO;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import util.WinnerChecker;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static util.Constants.REQUEST_PATH_SET_READY;

public class UBoatRoomContestController implements EncryptableByDictionary, WinnerChecker<CandidateDataDTO> {

    private UBoatMainAppController parentController;
    @FXML private BorderPane machineConfigurationComponent;
    @FXML private MachineConfigurationController machineConfigurationComponentController;
    @FXML private GridPane encryptComponent;
    @FXML private EncryptController encryptComponentController;
    @FXML private Button readyButton;
    @FXML private Button logoutButton;
    //@FXML private ScrollPane candidatesTableComponent;
    @FXML private AnchorPane connectedTeamsPlace;
    @FXML private HBox candidatesTablePlace;
    private CandidatesTableController candidatesTableComponentController;
    //@FXML private GridPane connectedTeamsComponent;
    private ConnectedUsersController connectedTeamsComponentController;
    private DictionaryDTO dictionaryDetails;
    private final BooleanProperty isPrepareForContest = new SimpleBooleanProperty(false);

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
            encryptComponent.disableProperty().bind(isPrepareForContest);
            parentController.getEncryptedMessageProperty().addListener(
                    (observable, oldValue, newValue) ->  encryptComponentController.setEncryptedMessageLabel(newValue));
        }

        try {
            FXMLLoader load = new FXMLLoader();
            load.setLocation(ConnectedUsersController.class.getResource("ConnectedUsersComponent.fxml"));
            Node usersComponent = load.load();
            connectedTeamsPlace.getChildren().add(usersComponent);
            DynamicComponent.fitToPane(usersComponent);
            connectedTeamsComponentController = load.getController();


            load = new FXMLLoader();
            load.setLocation(CandidatesTableController.class.getResource("candidates-table.fxml"));
            Node candidatesComponent = load.load();
            candidatesTablePlace.getChildren().add(candidatesComponent);
            DynamicComponent.fitToPane(candidatesComponent);
            candidatesTableComponentController = load.getController();

            candidatesTableComponentController.setWinnerChecker(this);

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
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
        parentController.close();
    }

    @FXML
    void readyButtonListener(ActionEvent event) {

        String encryptedMessage = encryptComponentController.getEncryptedMessage().get();

        if(encryptedMessage == null || encryptedMessage.isEmpty()){
            parentController.showPopUpMessage("You still not encrypt message");
        }
        else {
            isPrepareForContest.set(true);
            String finalUrl = HttpUrl
                    .parse(REQUEST_PATH_SET_READY)
                    .newBuilder()
                    .addQueryParameter("encryptedMessage", encryptedMessage)
                    .build()
                    .toString();

            HttpClientUtil.runAsyncGet(finalUrl, new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    System.out.println(" FAILURE ---- Could not response well" + e.getMessage());
                    isPrepareForContest.set(false);
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                    if (response.code() == 200) {
                        candidatesTableComponentController.startListRefresher(null);
                        System.out.println("encrypted message was updated and now the server is waiting for the teams to set ready!");
                    } else {
                        isPrepareForContest.set(false);
                        parentController.showPopUpMessage(response.code() + " " + response.body().string());
                    }
                }
            });
        }
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

    public void clearComponent(){

        clearAfterContest();
        machineConfigurationComponentController.clearComponent();
        dictionaryDetails = null;
    }

    public void setActive() {
        connectedTeamsComponentController.startListRefresher(constants.Constants.REQUEST_PATH_USERS_UPDATE);
        candidatesTableComponentController.clear();
        isPrepareForContest.set(false);
    }

    public BooleanProperty isPrepareForContestProperty() {
        return isPrepareForContest;
    }

    @Override
    public void checkIfWinner(CandidateDataDTO arg) {

        if(encryptComponentController.getMessageToEncrypt().equals(arg.getDecryptedMessage())){
            finishContest(arg);
        }
    }

    private void finishContest(CandidateDataDTO winnerCandidate) {
        setActive();
        clearAfterContest();
        parentController.showPopUpMessage("the winner is: " + winnerCandidate.getFoundersName());
        parentController.announceTheWinner(winnerCandidate);
    }

    private void clearAfterContest(){
        candidatesTableComponentController.clear();
        connectedTeamsComponentController.clearComponent();
        encryptComponentController.clearButtonActionListener(new ActionEvent());
    }
}
