package logic;

import components.UBoatAppMainController;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import logic.events.CodeSetEventListener;
import logic.events.handler.MachineEventHandler;
import machineDtos.MachineInfoDTO;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class UBoatLogicUI {
    private UBoatLogic uBoatLogic;
    private UBoatAppMainController appController;
    private final StringProperty encryptedMessage = new SimpleStringProperty();
    public MachineEventHandler<CodeSetEventListener> codeSetEventHandler = new MachineEventHandler<>();

    public StringProperty getEncryptedMessageProperty() {

        return encryptedMessage;
    }

    public UBoatLogicUI(UBoatAppMainController controller){

        appController = controller;
    }

    public void loadNewXmlFile(String path) {

        try {
            InputStream xmlFile = Files.newInputStream(Paths.get(path));
            //machine.loadXmlFile(xmlFile);
            uBoatLogic.uploadFileToServer(path, xmlFile.toString());
            appController.setIsGoodFileSelected(true);
        } catch (Exception | Error e) {
            appController.setSelectedFile("-");
            appController.setIsGoodFileSelected(false);
            appController.showPopUpMessage(e.getMessage());
        }
    }

    public void manualInitialCodeConfiguration(MachineInfoDTO initialArgs) throws IOException {

        //machine.manualMachineInit(initialArgs);
        //codeSetEventHandler.fireEvent(machine.displayingMachineSpecification());
        //appController.setMachineSpecification(machine.displayingMachineSpecification());
        uBoatLogic.updateMachineConfiguration(initialArgs);
    }



}
