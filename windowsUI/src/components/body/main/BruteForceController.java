package components.body.main;

import components.body.details.DecryptionManagerController;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import machineDtos.EngineDTO;
import manager.DecryptionManager;

public class BruteForceController implements encryptParentController {

    private BodyController parentController;
    @FXML private GridPane encryptComponent;
    @FXML private EncryptController encryptComponentController;
    @FXML private GridPane decryptionManagerComponent;
    @FXML private DecryptionManagerController decryptionManagerComponentController;
    private BooleanProperty encryptedWordIsAvailable = new SimpleBooleanProperty(false);


    public void setParentController(BodyController controller){
        parentController = controller;
    }

    @FXML public void initialize(){

        decryptionManagerComponentController.setParentController(this);
        encryptedWordIsAvailable.bind(encryptComponentController.getEncryptedMessage().isNotEqualTo(""));
        //decryptionManagerComponent.disableProperty().bind(encryptedWordIsAvailable.not());
        encryptComponentController.setAutoStateOnly();
        encryptComponentController.setParentController(this);
    }

    public void setDecryptionManager(DecryptionManager decryptionManager) {
        decryptionManagerComponentController.setDecryptionManager(decryptionManager);
        decryptionManagerComponentController.initial();
    }

    public String getEncryptedMessage(){
//        return encryptComponentController.getEncryptedMessage().getValue();
        return "XYV";
    }

    @Override
    public EngineDTO getEngineDetails() {
        return parentController.getEngineDetails();
    }
}
