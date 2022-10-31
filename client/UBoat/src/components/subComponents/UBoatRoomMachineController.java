package components.subComponents;

import components.body.details.CodeCalibrationController;
import components.body.details.EngineDetailsController;
import components.body.main.EngineDtoReturnableParentController;
import components.main.UBoatMainAppController;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import machineDtos.EngineDTO;

public class UBoatRoomMachineController implements EngineDtoReturnableParentController {

    private UBoatMainAppController parentController;
    @FXML private BorderPane engineDetailsComponent;
    @FXML private EngineDetailsController engineDetailsComponentController;
    @FXML private BorderPane codeCalibrationComponent;
    @FXML private CodeCalibrationController codeCalibrationComponentController;

    public void initial(){
        if (codeCalibrationComponent != null){
            codeCalibrationComponentController.setParentController(this);
            setListenerForInitialCode();
            parentController.addListenerForCodeSet(codeCalibrationComponentController);
        }
        if(engineDetailsComponent != null){
            engineDetailsComponentController.setParentController(this);
        }
    }

    public void setParentController(UBoatMainAppController parentController) {
        this.parentController = parentController;
    }

    public void setEngineDetailsInComponents(EngineDTO engine) {
        engineDetailsComponentController.initialComponent(engine.getEngineComponentsInfo());
        codeCalibrationComponentController.initialComponent(engine);
    }

    public void clearDetails(){
        codeCalibrationComponentController.clearComponent();
        engineDetailsComponentController.clearComponent();
    }

    @Override
    public EngineDTO getEngineDetails() {

        return parentController.getEngineDetails();
    }

    public void setListenerForInitialCode(){

        codeCalibrationComponentController.getRandomButtonOnActionListener().set(
                observable -> parentController.initialMachineConfiguration(null));

        codeCalibrationComponentController.getMachineInfoProperty().addListener(
                (observable, oldValue, newValue) ->  parentController.initialMachineConfiguration(newValue));
    }

    public void disableCodeCalibration(){
        codeCalibrationComponent.setDisable(true);
    }

    //not sure if need but:
    public void enableCodeCalibration(){
        codeCalibrationComponent.setDisable(false);
    }

}
