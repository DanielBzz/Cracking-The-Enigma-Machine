package components.body.main;

import components.body.details.CodeCalibrationController;
import components.body.details.EngineDetailsController;
import components.body.details.MachineConfigurationController;
import components.main.EnigmaAppController;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import logic.CodeSetEventHandler;
import machineDtos.EngineInfoDTO;
import machineDtos.MachineInfoDTO;

public class BodyController {

    @FXML
    private BorderPane codeCalibrationComponent;
    @FXML private CodeCalibrationController codeCalibrationComponentController;
    @FXML
    private BorderPane machineConfigurationComponent;
    @FXML private MachineConfigurationController machineConfigurationComponentController;
    @FXML private BorderPane engineDetailsComponent;
    @FXML private EngineDetailsController engineDetailsComponentController;
    private EnigmaAppController mainController;
    private EngineInfoDTO engineDetails;
    public CodeSetEventHandler codeSetEvent = new CodeSetEventHandler();

    public void initial() {

        if(codeCalibrationComponentController != null && machineConfigurationComponentController != null &&
        engineDetailsComponentController != null){
            codeCalibrationComponentController.setParentController(this);
            machineConfigurationComponentController.setParentController(this);
            engineDetailsComponentController.setParentController(this);
            machineConfigurationComponent.disableProperty().bind(codeCalibrationComponentController.getIsCodeConfigurationSetProperty().not());
        }

       // codeSetEvent.addListener(machineConfigurationComponentController);
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

    public void randomCodeClicked(){

        mainController.initialRandomCode();;
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
}


