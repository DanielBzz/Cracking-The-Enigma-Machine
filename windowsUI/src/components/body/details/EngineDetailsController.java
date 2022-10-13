package components.body.details;

import components.body.machine.DynamicMachineComponentFactory;
import components.body.machine.ReflectorController;
import components.body.machine.RotorController;
import components.body.main.EngineDtoReturnableParentController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import machineDtos.EngineComponentsCTEDTO;
import scheme.generated.CTEReflector;
import scheme.generated.CTERotor;

import java.util.List;

public class EngineDetailsController implements RotorParent, ReflectorParent{

    EngineDtoReturnableParentController parentController;
    @FXML private HBox rotorsPane;
    @FXML private HBox reflectorsPane;
    @FXML private FlowPane abcPane;

    public void setParentController(EngineDtoReturnableParentController controller){
        parentController = controller;
    }

    public void initialComponent(EngineComponentsCTEDTO engineDetails){

        setReflectorPane(engineDetails.getOptionalReflectors());
        setRotorsPane(engineDetails.getOptionalRotors());
        setAbcPane(engineDetails.getABC());
    }

    private void setRotorsPane(List<CTERotor> optionalRotors){

        rotorsPane.getChildren().clear();
        for(int i=0 ; i < optionalRotors.size(); i++){

            RotorController newController = DynamicMachineComponentFactory.createRotorOnPane(rotorsPane,this);
            newController.setRotorIdChoiceBox(i+1);
        }
    }

    private void setReflectorPane(List<CTEReflector> optionalReflectors){

        reflectorsPane.getChildren().clear();
        for (CTEReflector reflector: optionalReflectors){

            ReflectorController newReflector = DynamicMachineComponentFactory.createReflectorOnPane(reflectorsPane, this);
            newReflector.setCurrentReflector(reflector);
        }
    }

    private void setAbcPane(String abc){

        for (Character c: abc.toCharArray()) {

            Label charLabel = new Label(c.toString());
            abcPane.getChildren().add(charLabel);
        }

    }

    @Override
    public CTERotor getRotor(int id) {

        return parentController.getEngineDetails().getEngineComponentsInfo().getOptionalRotors().stream().
                filter(rotor -> rotor.getId() == id).findFirst().get();
    }

    public void clearComponent() {
        rotorsPane.getChildren().clear();
        reflectorsPane.getChildren().clear();
        abcPane.getChildren().clear();
    }
}
