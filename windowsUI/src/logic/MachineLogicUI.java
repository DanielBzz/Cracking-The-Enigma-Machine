package logic;

import components.main.EnigmaAppController;
import exceptions.MultipleMappingException;
import machineDtos.EngineInfoDTO;

public class MachineLogicUI implements EnigmaMachineUI {

    private EnigmaAppController appController;
    private EnigmaSystemEngine machine = new EnigmaEngine();

    public MachineLogicUI(EnigmaAppController controller){
        appController = controller;
    }

    @Override
    public void loadNewXmlFile() {

        try {
            machine.loadXmlFile(appController.getSelectedFile());
        } catch (Exception | Error e) {
            appController.setSelectedFile("-");
            appController.showPopUpMessage(e.getMessage());
        }
    }

    @Override
    public void displayingMachineSpecification() {

        EngineInfoDTO machineSpecification = machine.displayingMachineSpecification();
        appController.setMachineSpecification(machineSpecification);
    }

    @Override
    public void manualInitialCodeConfiguration() throws MultipleMappingException {

    }

    @Override
    public void automaticInitialCodeConfiguration() {

    }

    @Override
    public void encryptInput() {

    }

    @Override
    public void resetCurrentCode() {

    }

    @Override
    public void getHistoryAndStatistics() {

    }

    @Override
    public void exit() {

    }
}
