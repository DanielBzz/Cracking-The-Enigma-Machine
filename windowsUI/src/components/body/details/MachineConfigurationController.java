package components.body.details;

import components.Reflector;
import components.Rotor;
import components.body.machine.DynamicMachineComponentFactory;
import components.body.machine.ReflectorController;
import components.body.machine.RotorController;
import components.body.main.BodyController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import logic.events.CodeSetEventListener;
import machineDtos.EngineDTO;
import machineDtos.MachineInfoDTO;

import java.text.MessageFormat;

public class MachineConfigurationController implements CodeSetEventListener, ReflectorParent, RotorParent {

    BodyController parentController;
    @FXML private Label stringConfiguration;
    @FXML private StackPane reflectorPane;
    @FXML private HBox rotorsPane;
    @FXML private StackPane plugBoardPane;

    public void setParentController(BodyController controller){

        parentController = controller;
    }

    public void bind(MachineConfigurationController controller){

        stringConfiguration.textProperty().bind(controller.stringConfiguration.textProperty());
    }

    @Override
    public void invoke(EngineDTO updatedValue) {

        stringConfiguration.setText(currentMachineSpecification(updatedValue.getMachineCurrentInfo()));

        Reflector chosenReflector = updatedValue.getEngineComponentsInfo().getOptionalReflectors().stream().filter(
                reflector -> reflector.getId().equals(updatedValue.getMachineCurrentInfo().getReflectorID())).findFirst().get();
        ReflectorController controller = DynamicMachineComponentFactory.createReflectorOnPane(reflectorPane, this);
        controller.setCurrentReflector(chosenReflector);


        for (int i = 0; i<updatedValue.getNumOfUsedRotors(); i++) {

            int currentId = updatedValue.getMachineInitialInfo().getRotorsID().get(i);
            Rotor chosenRotor = updatedValue.getEngineComponentsInfo().getOptionalRotors().stream().filter(
                rotor -> rotor.getId() == currentId).findFirst().get();

            RotorController rotorController = DynamicMachineComponentFactory.createRotorOnPane(rotorsPane, this);
            rotorController.setCurrentRotor(chosenRotor,updatedValue.getMachineCurrentInfo().getRotorsInitPosition().get(i));
        }
        // logic finish ... should initial here the ui component and show them in that pane


        /*updatedValue.getMachineCurrentInfo().getPlugs()

        updatedValue.getMachineCurrentInfo().getNotchDistanceFromPositions()*/
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

    @Override
    public Rotor getRotor(int id) {
        return null;
    }
}
