package components.main;

import components.header.HeaderController;
import components.subComponents.UBoatRoomContestController;
import components.subComponents.UBoatRoomMachineController;
import contestDtos.CandidateDataDTO;
import decryptionDtos.DictionaryDTO;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.GridPane;
import logic.UBoatLogic;
import logic.events.CodeSetEventListener;
import machineDtos.EngineDTO;
import machineDtos.MachineInfoDTO;
import mainapp.AppMainController;
import mainapp.ClientMainController;

import static constants.Constants.AGENT_TYPE;
import static constants.Constants.UBOAT_TYPE;

public class UBoatMainAppController extends FileLoadable implements AppMainController, CodeSetEventListener {

    private ClientMainController parentController;
    @FXML private GridPane uBoatRoomMachineComponent;
    @FXML private UBoatRoomMachineController uBoatRoomMachineComponentController;
    @FXML private GridPane uBoatRoomContestComponent;
    @FXML private UBoatRoomContestController uBoatRoomContestComponentController;
    @FXML private ScrollPane headerComponent;
    @FXML private HeaderController headerComponentController;
    @FXML private Tab contestTab;
    private EngineDTO engineDetails;
    private UBoatLogic uBoatLogic;

    @FXML
    public void initialize() {

        uBoatLogic = new UBoatLogic(this);
        addListenerForCodeSet(this);
        if (headerComponentController != null && uBoatRoomMachineComponentController != null && uBoatRoomContestComponentController != null) {
            headerComponentController.setMainController(this);
            headerComponentController.initial();
            uBoatRoomMachineComponentController.setParentController(this);
            uBoatRoomMachineComponentController.initial();
            uBoatRoomMachineComponentController.codeCalibrationDisableProperty().bind(uBoatRoomContestComponentController.isPrepareForContestProperty());
            uBoatRoomContestComponentController.setParentController(this);
            uBoatRoomContestComponentController.initial();
            contestTab.disableProperty().bind(isGoodFileSelectedProperty().not());
            initialFileSelectedEvents();
        }
    }

    //---------------------from AppMainController ----------------------------
    @Override
    public void setClientMainController(ClientMainController clientMainController) {
        this.parentController = clientMainController;
    }

    @Override
    public void close(){
        uBoatLogic.logOut();
        clearComponent();
        parentController.switchToLogin();
    }

    @Override
    public void clearComponent(){
        uBoatRoomMachineComponentController.clearDetails();
        uBoatRoomContestComponentController.clearComponent();
    }

    //------------------------------------------- ----------------------------

    public EngineDTO getEngineDetails() {
        return engineDetails;
    }

    public void setEngineDetails(EngineDTO engine){
        this.engineDetails = engine;
    }

    public void initialFileSelectedEvents(){

        uBoatRoomMachineComponent.disableProperty().bind(isGoodFileSelectedProperty().not());
        uBoatRoomContestComponent.disableProperty().bind(isGoodFileSelectedProperty().not());
        selectedFileProperty().addListener((observable, oldValue, newValue) -> {
            setIsGoodFileSelected(false);
            clearComponent();
            uBoatLogic.uploadFileToServer(selectedFileProperty().get());
        });

        isGoodFileSelectedProperty().addListener((observable, oldValue, newValue) -> {

            if(newValue){
                uBoatRoomMachineComponentController.setEngineDetailsInComponents(engineDetails);
                uBoatRoomContestComponentController.setIsCodeConfigurationSet(false);
                uBoatRoomContestComponentController.setActive();
            }
        });
    }

    public void initialMachineConfiguration(MachineInfoDTO machineInfo){
        uBoatLogic.updateMachineConfiguration(machineInfo);
    }

    public void showPopUpMessage(String messageToShow){

        new Alert(Alert.AlertType.ERROR, messageToShow, ButtonType.OK).show();
    }

    public void addListenerForCodeSet(CodeSetEventListener listener){

        uBoatLogic.codeSetEventHandler.addListener(listener);
    }

    public void encryptMessage(String message){
        uBoatLogic.encryptInput(message);
    }

    @Override
    public void invoke(EngineDTO updatedValue) {
        this.engineDetails = updatedValue;
    }

    public void setDictionaryDetails(DictionaryDTO dictionary) {

        uBoatRoomContestComponentController.setDictionaryDetails(dictionary);
    }

    public StringProperty getEncryptedMessageProperty() {

        return uBoatLogic.encryptedMessageProperty();
    }

    public void announceTheWinner(CandidateDataDTO winnerCandidate) {
        uBoatLogic.finishContest(winnerCandidate);
    }
}
