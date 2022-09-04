package logic;

import components.main.EnigmaAppController;
import exceptions.MultipleMappingException;

public class MachineLogicUI implements EnigmaMachineUI {

    EnigmaAppController appController;
    EnigmaSystemEngine machine = new EnigmaEngine();

    public MachineLogicUI(EnigmaAppController controller){
        appController = controller;
    }

    @Override
    public void loadNewXmlFile() throws Exception {

        machine.loadXmlFile(appController.selectedFilePropertyProperty().get());
    }

    @Override
    public void displayingMachineSpecification() {

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
