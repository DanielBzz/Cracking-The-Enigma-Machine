package components.body.details;

import components.Reflector;
import components.Rotor;
import components.body.machine.DynamicMachineComponentFactory;
import components.body.machine.RotorController;
import components.body.main.BodyController;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import machineDtos.EngineInfoDTO;

import java.util.List;

public class EngineDetailsController implements RotorParent{

    BodyController parentController;

    @FXML private HBox rotorsPane;

    @FXML private HBox reflectorsPane;

    public void setParentController(BodyController controller){
        parentController = controller;
    }

    public void initialComponent(EngineInfoDTO engineDetails){

        setReflectorPane(engineDetails.getEngineComponentsInfo().getOptionalReflectors());
        setRotorsPane(engineDetails.getEngineComponentsInfo().getOptionalRotors());
    }

    private void setRotorsPane(List<Rotor> optionalRotors){

        for(int i=0 ; i < optionalRotors.size(); i++){

            RotorController newController = DynamicMachineComponentFactory.createRotorOnPane(rotorsPane,this);
            newController.setRotorIdChoiceBox(i+1);
        }
    }

    public void setReflectorPane(List<Reflector> optionalReflectors){



    }


    @Override
    public Rotor getRotor(int id) {

        return parentController.getEngineDetails().getEngineComponentsInfo().getOptionalRotors().stream().
                filter(rotor -> rotor.getId() == id).findFirst().get();
    }
}
