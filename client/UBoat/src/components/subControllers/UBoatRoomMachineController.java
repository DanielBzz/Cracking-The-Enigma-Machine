package components.subControllers;

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
    private EngineDTO engine;

    @FXML
    public void initialize(){
        if (codeCalibrationComponent != null){
            codeCalibrationComponentController.setParentController(this);
        }
        if(engineDetailsComponent != null){
            engineDetailsComponentController.setParentController(this);
        }
    }

    public void setParentController(UBoatMainAppController parentController) {
        this.parentController = parentController;
    }

    public void setEngine(EngineDTO engine) {
        this.engine = engine;
        engineDetailsComponentController.initialComponent(engine.getEngineComponentsInfo());
        codeCalibrationComponentController.initialComponent(engine);
    }

    public void clearDetails(){
        codeCalibrationComponentController.clearComponent();
        engineDetailsComponentController.clearComponent();
    }

    @Override
    public EngineDTO getEngineDetails() {
        return engine;
    }
}
