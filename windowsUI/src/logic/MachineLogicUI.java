package logic;

import components.main.EnigmaAppController;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import logic.events.CodeSetEventListener;
import logic.events.handler.MachineEventHandler;
import machineDtos.EngineDTO;
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

        EngineDTO machineSpecification = machine.displayingMachineSpecification();
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

        //encryptedMessage.set(machine.encryptString(msg));
        //codeSetEventHandler.fireEvent(machine.displayingMachineSpecification());
    }

    public void resetCurrentCode() {

        machine.resetTheMachine();
        codeSetEventHandler.fireEvent(machine.displayingMachineSpecification());
    }

    public void getHistoryAndStatistics() {

    }

    public void exit() {

    }

    /* problem to do fire event every time cause we do clone and duplicate every time the components --> create exception of space
*/
}
