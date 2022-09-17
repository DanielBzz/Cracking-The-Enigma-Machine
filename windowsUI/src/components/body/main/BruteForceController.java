package components.body.main;

import components.body.details.DecryptionManagerController;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import manager.DecryptionManager;

public class BruteForceController {

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
        decryptionManagerComponentController.initial(10,10000);
        encryptedWordIsAvailable.bind(encryptComponentController.getEncryptedMessage().isNotEqualTo(""));
        decryptionManagerComponent.disableProperty().bind(encryptedWordIsAvailable.not());
        encryptComponentController.setAutoStateOnly();
    }

    public void setDecryptionManager(DecryptionManager decryptionManager) {
        decryptionManagerComponentController.setDecryptionManager(decryptionManager);
    }

    public String getEncryptedMessage(){
        return encryptComponentController.getEncryptedMessage().getValue();
    }
}
