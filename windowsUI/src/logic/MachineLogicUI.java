package logic;

import components.main.EnigmaAppController;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import logic.events.CodeSetEventListener;
import logic.events.handler.MachineEventHandler;
import machineDtos.EngineInfoDTO;
import machineDtos.MachineInfoDTO;

public class MachineLogicUI {

    private EnigmaAppController appController;
    private EnigmaSystemEngine machine = new EnigmaEngine();
    private final StringProperty encryptedMessage = new SimpleStringProperty();
    public MachineEventHandler<CodeSetEventListener> codeSetEventHandler = new MachineEventHandler<>();

    public StringProperty getEncryptedMessageProperty() {
        return encryptedMessage;
    }

    public MachineLogicUI(EnigmaAppController controller){
        appController = controller;
    }

    public void loadNewXmlFile(String path) {

        try {
            machine.loadXmlFile(path);
        } catch (Exception | Error e) {
            appController.setSelectedFile("-");
            appController.showPopUpMessage(e.getMessage());
        }
    }

    public void displayingMachineSpecification() {

        EngineInfoDTO machineSpecification = machine.displayingMachineSpecification();
        appController.setMachineSpecification(machineSpecification);
    }

    public void manualInitialCodeConfiguration(MachineInfoDTO initialArgs) {

        machine.manualMachineInit(initialArgs);
        codeSetEventHandler.fireEvent(machine.displayingMachineSpecification());
    }


    public void automaticInitialCodeConfiguration() {

        machine.automaticMachineInit();
        codeSetEventHandler.fireEvent(machine.displayingMachineSpecification());
    }

    public void encryptInput(String msg) {

        encryptedMessage.set(machine.encryptString(msg));
    }

    public void resetCurrentCode() {

        machine.resetTheMachine();
    }

    public void getHistoryAndStatistics() {

    }

    public void exit() {

    }
}
