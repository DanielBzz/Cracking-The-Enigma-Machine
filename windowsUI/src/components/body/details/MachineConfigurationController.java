package components.body.details;

import components.body.main.BodyController;
import logic.CodeSetEventListener;
import machineDtos.EngineInfoDTO;

public class MachineConfigurationController implements CodeSetEventListener {

    BodyController parentController;
    public void setParentController(BodyController controller){
        parentController = controller;
    }


    @Override
    public void update(EngineInfoDTO updatedValue) {

    }
}
