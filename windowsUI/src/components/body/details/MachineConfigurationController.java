package components.body.details;

import components.body.main.BodyController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import logic.events.CodeSetEventListener;
import machineDtos.EngineInfoDTO;
import machineDtos.MachineInfoDTO;

import java.text.MessageFormat;

public class MachineConfigurationController implements CodeSetEventListener {

    BodyController parentController;
    @FXML private Label stringConfiguration;

    public void setParentController(BodyController controller){

        parentController = controller;
    }

    @Override
    public void invoke(EngineInfoDTO updatedValue) {

        stringConfiguration.setText(currentMachineSpecification(updatedValue.getMachineCurrentInfo()));

        // logic finish ... should initial here the ui component and show them in that pane

        /*updatedValue.getMachineCurrentInfo().getPlugs()
        updatedValue.getMachineCurrentInfo().getRotorsInitPosition()
        updatedValue.getMachineCurrentInfo().getReflectorID()
        updatedValue.getMachineCurrentInfo().getNotchDistanceFromPositions()
        updatedValue.getMachineCurrentInfo().getRotorsID()*/

    }

    private String currentMachineSpecification(MachineInfoDTO machineInfo){

        StringBuilder msg = new StringBuilder();

        msg.append("<");
        for (int i = machineInfo.getRotorsInitPosition().size() - 1; i >= 0; --i) {
            msg.append(MessageFormat.format("{0}({1}),", machineInfo.getRotorsID().get(i), machineInfo.getNotchDistanceFromPositions().get(i)));
        }

        msg.replace(msg.length() - 1, msg.length(),">");
        msg.append("<");
        for (int i = machineInfo.getRotorsInitPosition().size() - 1; i >= 0; --i) {
            msg.append(machineInfo.getRotorsInitPosition().get(i));
        }

        msg.append(">");
        msg.append(MessageFormat.format("<{0}>", machineInfo.getReflectorID()));
        if(machineInfo.getPlugs().size() != 0 ){
            msg.append("<");
            for (Character key : machineInfo.getPlugs().keySet()){

                msg.append(MessageFormat.format("{0}|{1},", key, machineInfo.getPlugs().get(key)));
            }

            msg.replace(msg.length() - 1, msg.length(),">");
        }

        return msg.toString();
    }
}
