package components.main;

import components.body.main.BodyController;
import components.header.HeaderController;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TabPane;
import logic.MachineLogicUI;
import logic.events.CodeSetEventListener;
import logic.events.EncryptMessageEventListener;
import logic.events.StatisticsUpdateEventListener;
import logic.events.handler.MachineEventHandler;
import machineDtos.EngineDTO;

public class EnigmaAppController extends FileLoadable{

    private MachineLogicUI machineUI;
    @FXML private ScrollPane headerComponent;
    @FXML private HeaderController headerComponentController;
    @FXML private TabPane bodyComponent;
    @FXML private BodyController bodyComponentController;

    public void initial() {
        if (headerComponentController != null && bodyComponentController != null) {
            headerComponentController.setMainController(this);
            headerComponentController.initial();
            bodyComponentController.setMainController(this);
            bodyComponentController.initial();

            initialFileSelectedEvents();
            initialControllerEventsToLogic();
        }
    }

    public void setMachineUI(MachineLogicUI machine){

        machineUI = machine;
    }

    public void showPopUpMessage(String messageToShow){

         new Alert(Alert.AlertType.ERROR, messageToShow, ButtonType.OK).show();
    }

    public void setMachineSpecification(EngineDTO details){

        bodyComponentController.setEngineDetails(details);
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

    public EncryptMessageEventListener getEncryptMessageEventListener(){
        return new EncryptMessageEventListener() {
            @Override
            public void invoke(String arg) {
                machineUI.encryptInput(arg);
            }
        };
    }

    public void initialFileSelectedEvents(){

        bodyComponent.disableProperty().bind(isGoodFileSelectedProperty().not());
        selectedFileProperty().addListener((observable, oldValue, newValue) -> {
            setIsGoodFileSelected(false);
            clearStage();
            machineUI.loadNewXmlFile(newValue);
        });

        isGoodFileSelectedProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue){
                machineUI.displayingMachineSpecification();
                bodyComponentController.initialCodeCalibration();
                bodyComponentController.initialEngineDetails();
                bodyComponentController.setDecryptionManager(machineUI.getMachineEngine());
                bodyComponentController.setIsCodeConfigurationSet(false);
            }
        });
    }

    private void initialControllerEventsToLogic(){

        bodyComponentController.getMachineInfoProperty().addListener(
                (observable, oldValue, newValue) -> machineUI.manualInitialCodeConfiguration(newValue));

        bodyComponentController.setListenerToHistoryUpdate(machineUI);
        bodyComponentController.codeCalibrationRandomCodeOnAction().set(observable -> machineUI.automaticInitialCodeConfiguration());
        bodyComponentController.encryptResetButtonActionProperty().set(observable -> machineUI.resetCurrentCode());
    }

    private void clearStage(){

        bodyComponentController.clearComponent();
    }
}
