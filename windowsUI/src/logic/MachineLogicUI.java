package logic;

import components.main.EnigmaAppController;
import machineDtos.EngineInfoDTO;
import machineDtos.MachineInfoDTO;

public class MachineLogicUI  {

    private EnigmaAppController appController;
    private EnigmaSystemEngine machine = new EnigmaEngine();

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
    }


    public void automaticInitialCodeConfiguration() {

        machine.automaticMachineInit();
    }

    public void encryptInput() {

    }

    public void resetCurrentCode() {

    }

    public void getHistoryAndStatistics() {

    }

    public void exit() {

    }
}
