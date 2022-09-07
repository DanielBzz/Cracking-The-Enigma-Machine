package components.body.main;

import components.body.details.CodeCalibrationController;
import components.body.details.MachineConfigurationController;
import components.main.EnigmaAppController;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import machineDtos.EngineInfoDTO;

public class BodyController {

    @FXML
    private BorderPane codeCalibrationComponent;
    @FXML private CodeCalibrationController codeCalibrationComponentController;
    @FXML
    private BorderPane machineConfigurationComponent;
    @FXML private MachineConfigurationController machineConfigurationComponentController;
    private EnigmaAppController mainController;
    private EngineInfoDTO engineDetails;

    public void initial() {

        if(codeCalibrationComponentController != null && machineConfigurationComponentController != null){
            codeCalibrationComponentController.setParentController(this);
            machineConfigurationComponentController.setParentController(this);
        }
        else{
            System.out.println("nullll");
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
        codeCalibrationComponentController.initialComponent();
    }

}

