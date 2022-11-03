package components.subComponents;

import components.DynamicComponent;
import components.body.details.CodeCalibrationController;
import components.body.details.EngineDetailsController;
import components.body.main.EngineDtoReturnableParentController;
import components.main.UBoatMainAppController;
import javafx.beans.property.BooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import machineDtos.EngineDTO;

import java.io.IOException;

public class UBoatRoomMachineController implements EngineDtoReturnableParentController {

    private UBoatMainAppController parentController;
    @FXML private AnchorPane engineDetailsPlace;
    private BorderPane engineDetailsComponent;
    private EngineDetailsController engineDetailsComponentController;
    @FXML private AnchorPane codeCalibrationPlace;
    private BorderPane codeCalibrationComponent;
    private CodeCalibrationController codeCalibrationComponentController;

    public void initial(){

        try {
            FXMLLoader load = new FXMLLoader();
            load.setLocation(EngineDetailsController.class.getResource("engineDetailsComponent.fxml"));
            engineDetailsComponent = load.load();
            engineDetailsPlace.getChildren().add(engineDetailsComponent);
            DynamicComponent.fitToPane(engineDetailsComponent);
            engineDetailsComponentController = load.getController();

            load = new FXMLLoader();
            load.setLocation(CodeCalibrationController.class.getResource("codeCalibrationComponent.fxml"));
            codeCalibrationComponent = load.load();
            codeCalibrationPlace.getChildren().add(codeCalibrationComponent);
            DynamicComponent.fitToPane(codeCalibrationComponent);
            codeCalibrationComponentController = load.getController();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

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

    public BooleanProperty codeCalibrationDisableProperty(){
        return codeCalibrationComponent.disableProperty();
    }

}
