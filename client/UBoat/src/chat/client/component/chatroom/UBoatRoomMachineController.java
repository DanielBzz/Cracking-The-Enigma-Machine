package chat.client.component.chatroom;

import components.body.details.CodeCalibrationController;
import components.body.details.EngineDetailsController;
import components.body.main.EngineDtoReturnableParentController;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import machineDtos.EngineDTO;
import machineDtos.MachineInfoDTO;

public class UBoatRoomMachineController implements EngineDtoReturnableParentController {

    private UBoatRoomController parentController;

    @FXML
    private EngineDetailsController engineDetailsComponent;

    @FXML
    private CodeCalibrationController codeCalibrationComponent;
    private EngineDTO engine;

    public void initial(){
        if (codeCalibrationComponent != null){
            codeCalibrationComponent.setParentController(this);
        }
        if(engineDetailsComponent != null){
            engineDetailsComponent.setParentController(this);
        }
    }

    public void setUBoatRoomController(UBoatRoomController uBoatRoomController) {
        this.parentController = uBoatRoomController;
    }

    public void setEngine(EngineDTO engine) {
        this.engine = engine;
    }
    public void initialCodeCalibration(){
        codeCalibrationComponent.initialComponent(engine);
    }
    public void initialEngineDetails(){
        engineDetailsComponent.initialComponent(engine);
    }

    public SimpleObjectProperty<MachineInfoDTO> getMachineInfoProperty(){
        return codeCalibrationComponent.getMachineInfoProperty();
    }

    public ObjectProperty<EventHandler<ActionEvent>> codeCalibrationRandomCodeOnAction(){

        return codeCalibrationComponent.getRandomButtonOnActionListener();
    }

    public void clearDetails(){
        codeCalibrationComponent.clearComponent();
        engineDetailsComponent.clearComponent();
    }
    @Override
    public EngineDTO getEngineDetails() {
        return engine;
    }
}
