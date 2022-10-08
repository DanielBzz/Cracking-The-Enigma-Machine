package chat.client.component.chatroom;

import chat.client.component.main.UBoatAppMainController;
import components.body.main.BodyController;
import components.header.HeaderController;
import components.main.FileLoadable;
import javafx.beans.property.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TabPane;
import logic.EnigmaSystemEngine;
import logic.HistoryUpdatable;
import logic.MachineLogicUI;
import logic.events.CodeSetEventListener;
import logic.events.EncryptMessageEventListener;
import logic.events.StatisticsUpdateEventListener;
import logic.events.handler.MachineEventHandler;
import machineDtos.EngineDTO;
import machineDtos.MachineInfoDTO;

public class UBoatRoomController implements FileLoadable{

    private UBoatAppMainController parentController;
    @FXML
    private UBoatRoomMachineController uBoatRoomMachineController;
    @FXML
    private UBoatRoomContestController uBoatRoomContestController;
    @FXML private HeaderController headerComponentController;
    @FXML private ScrollPane headerComponent;
    @FXML private TabPane bodyComponent;
    public void setUBoatAppMainController(UBoatAppMainController uBoatAppMainController) {
        this.parentController = uBoatAppMainController;
    }

//    @FXML private ScrollPane headerComponent;

//    @FXML private BodyController bodyComponentController;
    private final SimpleStringProperty selectedFileProperty = new SimpleStringProperty("-");
    private final SimpleBooleanProperty isGoodFileSelected = new SimpleBooleanProperty(false);

    public void initial() {

        if (headerComponentController != null && uBoatRoomMachineController != null && uBoatRoomContestController!= null) {
            headerComponentController.setMainController(this);
            headerComponentController.initial();
            uBoatRoomMachineController.setUBoatRoomController(this);
            uBoatRoomMachineController.initial();
            uBoatRoomContestController.setUBoatRoomController(this);
            uBoatRoomContestController.initial();

            setLogicEventsToController();
            initialFileSelectedEvents();
            initialControllerEventsToLogic();
        }
    }
    @Override
    public SimpleStringProperty selectedFileProperty() {

        return this.selectedFileProperty;
    }
    @Override
    public void setSelectedFile(String selectedFilePath) {

        selectedFileProperty.set(selectedFilePath);
    }

    public void showPopUpMessage(String messageToShow){

        new Alert(Alert.AlertType.ERROR, messageToShow, ButtonType.OK).show();
    }

    public void setMachineSpecification(EngineDTO details){

        uBoatRoomMachineController.setEngine(details);
        uBoatRoomContestController.setEngine(details);
        //bodyComponentController.setEngineDetails(details);
    }

    public MachineEventHandler<CodeSetEventListener> CodeSetEventHandler()  {

        return machineUI.codeSetEventHandler;
    }

    public MachineEventHandler<StatisticsUpdateEventListener> statisticsUpdateEventHandler()  {

        return machineUI.statisticsUpdateEventHandler;
    }

    public StringProperty getMachineEncryptedMessageProperty(){

        return machineUI.getEncryptedMessageProperty();
    }

    public void setIsGoodFileSelected(Boolean isGood) {

        isGoodFileSelected.set(isGood);
    }

    public EncryptMessageEventListener getEncryptMessageEventListener(){
        return new EncryptMessageEventListener() {
            @Override
            public void invoke(String arg) {
                machineUI.encryptInput(arg);
            }
        };
    }

    private void initialFileSelectedEvents(){

        bodyComponent.disableProperty().bind(isGoodFileSelected.not());
        selectedFileProperty.addListener((observable, oldValue, newValue) -> {
            isGoodFileSelected.set(false);
            clearStage();
            machineUI.loadNewXmlFile(newValue);
        });

        isGoodFileSelected.addListener((observable, oldValue, newValue) -> {
            if(newValue){
                machineUI.displayingMachineSpecification();

                //bodyComponentController.initialCodeCalibration();
                uBoatRoomMachineController.initialCodeCalibration();
                //bodyComponentController.initialEngineDetails();
                uBoatRoomMachineController.initialEngineDetails();
                bodyComponentController.setDecryptionManager(machineUI.getMachineEngine());
                //need to find a solution for this ^^^
                //bodyComponentController.setIsCodeConfigurationSet(false);
                uBoatRoomContestController.setIsCodeConfigurationSet(false);
            }
        });
    }

    private void initialControllerEventsToLogic(){

        uBoatRoomMachineController.getMachineInfoProperty().addListener(
                (observable, oldValue, newValue) -> machineUI.manualInitialCodeConfiguration(newValue));

        //bodyComponentController.setListenerToHistoryUpdate(machineUI);
        uBoatRoomMachineController.codeCalibrationRandomCodeOnAction().set(observable -> machineUI.automaticInitialCodeConfiguration());
        uBoatRoomContestController.encryptResetButtonActionProperty().set(observable -> machineUI.resetCurrentCode());
    }

    private void clearStage(){

        bruteForceComponentController.clearComponent();
        //need to find a solution for this ^^^
        uBoatRoomMachineController.clearDetails();
        uBoatRoomContestController.clearDetails();
        //bodyComponentController.clearComponent();
    }






    public void setEngineDetails(EngineDTO details) {

        engine = details;
    }

    public ObjectProperty<EventHandler<ActionEvent>> codeCalibrationRandomCodeOnAction(){

        return codeCalibrationComponentController.getRandomButtonOnActionListener();
    }

    public SimpleObjectProperty<MachineInfoDTO> getMachineInfoProperty(){
        return codeCalibrationComponentController.getMachineInfoProperty();
    }



    public ObjectProperty<EventHandler<ActionEvent>> encryptResetButtonActionProperty(){

        return encryptComponentController.getResetButtonActionProperty();
    }

    public void setDecryptionManager(EnigmaSystemEngine engine){
        bruteForceComponentController.setDecryptionManager(new DecryptionManager(engine));
    }


    public SimpleBooleanProperty getIsCodeConfigurationSetProperty(){
        return machineConfigurationComponentController.getIsCodeConfigurationSetProperty();
    }

    public void setListenerToHistoryUpdate(HistoryUpdatable listener){
        encryptComponentController.setHistoryUpdatable(listener);
    }

    public StringProperty getMachineEncryptedMessageProperty(){
        return mainController.getMachineEncryptedMessageProperty();
    }

    private void setLogicEventsToController(){

        mainController.statisticsUpdateEventHandler().addListener(statisticsComponentController);
        mainController.getMachineEncryptedMessageProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if(encryptTab.isSelected()) {
                        encryptComponentController.setEncryptedMessageLabel(newValue);
                    } else {
                        bruteForceComponentController.setEncryptedMessageLabel(newValue);
                    }
                });
        mainController.CodeSetEventHandler().addListener(bruteForceComponentController);
        mainController.CodeSetEventHandler().addListener(codeCalibrationComponentController);
        mainController.CodeSetEventHandler().addListener(machineConfigurationComponentController);

    }
}
