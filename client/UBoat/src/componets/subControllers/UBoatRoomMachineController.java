package componets.subControllers;

import components.body.details.CodeCalibrationController;
import components.body.details.EngineDetailsController;
import components.body.main.EngineDtoReturnableParentController;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import machineDtos.EngineDTO;
import machineDtos.MachineInfoDTO;

public class UBoatRoomMachineController implements EngineDtoReturnableParentController {

    private UBoatRoomController parentController;

    @FXML
    private EngineDetailsController engineDetailsComponentController;
    @FXML
    private BorderPane engineDetailsComponent;
    @FXML
    private CodeCalibrationController codeCalibrationComponentController;
    @FXML
    private BorderPane codeCalibrationComponent;
    private EngineDTO engine;

    public void initial(){
        if (codeCalibrationComponent != null){
            codeCalibrationComponentController.setParentController(this);
        }
        if(engineDetailsComponent != null){
            engineDetailsComponentController.setParentController(this);
        }
    }

    public void setUBoatRoomController(UBoatRoomController uBoatRoomController) {
        this.parentController = uBoatRoomController;
    }

    public void setEngine(EngineDTO engine) {
        this.engine = engine;
    }

    public CodeCalibrationController getCodeCalibrationComponentController(){
        return codeCalibrationComponentController;
    }
    public void initialCodeCalibration(){
        codeCalibrationComponentController.initialComponent(engine);
    }
    public void initialEngineDetails(){
        engineDetailsComponentController.initialComponent(engine);
    }

    public SimpleObjectProperty<MachineInfoDTO> getMachineInfoProperty(){
        return codeCalibrationComponentController.getMachineInfoProperty();
    }

    public ObjectProperty<EventHandler<ActionEvent>> codeCalibrationRandomCodeOnAction(){

        return codeCalibrationComponentController.getRandomButtonOnActionListener();
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
