package components.body.main;

import components.body.details.CodeCalibrationController;
import components.body.details.EngineDetailsController;
import components.body.details.MachineConfigurationController;
import components.body.details.StatisticsController;
import components.main.EnigmaAppController;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import logic.DecipherLogic;
import logic.EnigmaEngine;
import logic.EnigmaSystemEngine;
import logic.HistoryUpdatable;
import logic.events.EncryptMessageEventListener;
import machineDtos.EngineDTO;
import machineDtos.MachineInfoDTO;


public class BodyController implements EncryptParentController, EngineDtoReturnableParentController {

    private EnigmaAppController mainController;
    @FXML private BorderPane codeCalibrationComponent;
    @FXML private CodeCalibrationController codeCalibrationComponentController;
    @FXML private BorderPane machineConfigurationComponent;
    @FXML private MachineConfigurationController machineConfigurationComponentController;
    @FXML private BorderPane engineDetailsComponent;
    @FXML private EngineDetailsController engineDetailsComponentController;
    @FXML private BorderPane encryptScreenMachineConfigurationComponent;
    @FXML private MachineConfigurationController encryptScreenMachineConfigurationComponentController;
    @FXML private GridPane encryptComponent;
    @FXML private EncryptController encryptComponentController;
    @FXML private ScrollPane bruteForceComponent;
    @FXML private  BruteForceController bruteForceComponentController;
    @FXML private ScrollPane statisticsComponent;
    @FXML private StatisticsController statisticsComponentController;
    @FXML private Tab encryptTab;
    private EngineDTO engineDetails;

    public void initial() {

        if (codeCalibrationComponentController != null){
            codeCalibrationComponentController.setParentController(this);
        }
        if(machineConfigurationComponentController != null){
            machineConfigurationComponentController.setParentController(this);
            machineConfigurationComponent.disableProperty().bind(machineConfigurationComponentController.getIsCodeConfigurationSetProperty().not());
            encryptScreenMachineConfigurationComponentController.bind(machineConfigurationComponentController);
            encryptComponent.disableProperty().bind(machineConfigurationComponentController.getIsCodeConfigurationSetProperty().not());
            encryptScreenMachineConfigurationComponent.disableProperty().bind(machineConfigurationComponent.disableProperty());
        }
        if(engineDetailsComponentController != null){
            engineDetailsComponentController.setParentController(this);
        }
        if(encryptComponentController != null) {
            encryptComponentController.setParentController(this);
            machineConfigurationComponentController.getIsCodeConfigurationSetProperty().addListener(observable -> encryptComponentController.createKeyboards(engineDetails.getEngineComponentsInfo().getABC()));
            initEncryptListener();
            initEncryptResetButtonActionListener();
        }
        if(encryptScreenMachineConfigurationComponentController!= null){
            encryptScreenMachineConfigurationComponentController.setParentController(this);
        }
        if(bruteForceComponentController!= null){
            bruteForceComponentController.setParentController(this);
            bruteForceComponentController.initial();
        }

        setLogicEventsToController();
    }

    public void setMainController(EnigmaAppController appController) {

        mainController = appController;
    }

    @Override
    public EngineDTO getEngineDetails(){

        return engineDetails;
    }
    public void setEngineDetails(EngineDTO details) {

        engineDetails = details;
    }

    public ObjectProperty<EventHandler<ActionEvent>> codeCalibrationRandomCodeOnAction(){

        return codeCalibrationComponentController.getRandomButtonOnActionListener();
    }

    public SimpleObjectProperty<MachineInfoDTO> getMachineInfoProperty(){
        return codeCalibrationComponentController.getMachineInfoProperty();
    }

    public void initialCodeCalibration(){

        codeCalibrationComponentController.initialComponent(engineDetails);
    }

    public void initialEngineDetails(){

        engineDetailsComponentController.initialComponent(engineDetails.getEngineComponentsInfo());
    }

    @Override
    public void initEncryptResetButtonActionListener(){

        encryptComponentController.getResetButtonActionProperty().addListener(observable -> resetCodeConfiguration());
    }

    @Override
    public void initEncryptListener() {
        encryptComponentController.activateEncryptEventHandler.addListener(getEncryptMessageEventListener());
    }

    public EncryptMessageEventListener getEncryptMessageEventListener(){
        return mainController.getEncryptMessageEventListener();
    }

    public void resetCodeConfiguration(){
        mainController.resetCodeConfiguration();
    }

    public void setDecryptionManager(EnigmaSystemEngine engine){
        bruteForceComponentController.setDecryptionManager(DecipherLogic.createDecryptionMangerFromDecipher((EnigmaEngine) engine));
    }

    public void setIsCodeConfigurationSet(Boolean codeSet){
        machineConfigurationComponentController.getIsCodeConfigurationSetProperty().set(codeSet);
    }

    public SimpleBooleanProperty getIsCodeConfigurationSetProperty(){
        return machineConfigurationComponentController.getIsCodeConfigurationSetProperty();
    }

    public void setListenerToHistoryUpdate(HistoryUpdatable listener){
        encryptComponentController.setHistoryUpdatable(listener);
    }

    public void clearComponent() {
        codeCalibrationComponentController.clearComponent();
        machineConfigurationComponentController.clearComponent();
        engineDetailsComponentController.clearComponent();
        encryptScreenMachineConfigurationComponentController.clearComponent();
        bruteForceComponentController.clearComponent();
        statisticsComponentController.clearController();
        encryptComponentController.clearButtonActionListener(new ActionEvent());
        encryptComponentController.removeOldAbcFromKeyboards();
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


