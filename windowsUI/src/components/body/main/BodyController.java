package components.body.main;

import components.body.details.CodeCalibrationController;
import components.body.details.EngineDetailsController;
import components.body.details.MachineConfigurationController;
import components.main.EnigmaAppController;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import logic.events.EncryptMessageEventListener;
import logic.events.handler.MachineEventHandler;
import machineDtos.EngineInfoDTO;
import machineDtos.MachineInfoDTO;

public class BodyController {

    private EnigmaAppController mainController;
    @FXML private BorderPane codeCalibrationComponent;
    @FXML private CodeCalibrationController codeCalibrationComponentController;
    @FXML private BorderPane machineConfigurationComponent;
    @FXML private MachineConfigurationController machineConfigurationComponentController;
    @FXML private BorderPane engineDetailsComponent;
    @FXML private EngineDetailsController engineDetailsComponentController;
    @FXML private BorderPane encryptScreenMachineConfigurationComponent;
    @FXML private GridPane encryptComponent;
    @FXML private EncryptController encryptComponentController;

    private EngineInfoDTO engineDetails;

    public void initial() {

        if (codeCalibrationComponentController != null && machineConfigurationComponentController != null &&
                engineDetailsComponentController != null && encryptComponentController != null) {
            codeCalibrationComponentController.setParentController(this);
            machineConfigurationComponentController.setParentController(this);
            engineDetailsComponentController.setParentController(this);
            encryptComponentController.setParentController(this);
            machineConfigurationComponent.disableProperty().bind(codeCalibrationComponentController.getIsCodeConfigurationSetProperty().not());
            mainController.CodeSetEventHandler().addListener(machineConfigurationComponentController);
            mainController.getMachineEncryptedMessageProperty().addListener(
                    (observable, oldValue, newValue) -> encryptComponentController.setEncryptedMessageLabel(newValue));
        }
    }

    public void setMainController(EnigmaAppController appController) {

        mainController = appController;
    }

    public EngineInfoDTO getEngineDetails(){

        return engineDetails;
    }
    public void setEngineDetails(EngineInfoDTO details) {

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

        engineDetailsComponentController.initialComponent(engineDetails);
    }

    public MachineEventHandler<EncryptMessageEventListener> encryptMessageEventHandler()  {

        return encryptComponentController.activateEncryptEventHandler;
    }
}


