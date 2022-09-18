package logic;

import components.main.EnigmaAppController;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import logic.events.CodeSetEventListener;
import logic.events.StatisticsUpdateEventListener;
import logic.events.handler.MachineEventHandler;
import machineDtos.EngineDTO;
import machineDtos.MachineInfoDTO;

public class MachineLogicUI {

    private EnigmaAppController appController;
    private EnigmaSystemEngine machine = new EnigmaEngine();
    private final StringProperty encryptedMessage = new SimpleStringProperty();
    public MachineEventHandler<CodeSetEventListener> codeSetEventHandler = new MachineEventHandler<>();
    public MachineEventHandler<StatisticsUpdateEventListener> statisticsUpdateEventHandler = new MachineEventHandler<>();


    public StringProperty getEncryptedMessageProperty() {

        return encryptedMessage;
    }

    public MachineLogicUI(EnigmaAppController controller){

        appController = controller;
    }

    public void loadNewXmlFile(String path) {

        try {
            machine.loadXmlFile(path);
            appController.setIsGoodFileSelected(true);
        } catch (Exception | Error e) {
            appController.setSelectedFile("-");
            appController.setIsGoodFileSelected(false);
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
        appController.setMachineSpecification(machine.displayingMachineSpecification());

    }

    public void automaticInitialCodeConfiguration() {

        machine.automaticMachineInit();
        codeSetEventHandler.fireEvent(machine.displayingMachineSpecification());
        appController.setMachineSpecification(machine.displayingMachineSpecification());

    }

    public void encryptInput(String msg) {

        encryptedMessage.set(machine.encryptString(msg));
        codeSetEventHandler.fireEvent(machine.displayingMachineSpecification());
        statisticsUpdateEventHandler.fireEvent(machine.getHistoryAndStatistics());
    }

    public void resetCurrentCode() {

        machine.resetTheMachine();
        codeSetEventHandler.fireEvent(machine.displayingMachineSpecification());
    }

    public void getHistoryAndStatistics() {

        machine.getHistoryAndStatistics();
    }

    public void exit() {

    }

    public EnigmaSystemEngine getMachineEngine(){
        return machine;
    }
    /* problem to do fire event every time cause we do clone and duplicate every time the components --> create exception of space
*/
}
