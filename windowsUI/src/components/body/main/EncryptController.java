package components.body.main;

import com.gluonhq.charm.glisten.control.ToggleButtonGroup;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import logic.events.EncryptMessageEventListener;
import logic.events.handler.MachineEventHandler;

public class EncryptController {

    private BodyController parentController;
    @FXML private Button processButton;
    @FXML private Button doneButton;
    @FXML private Button clearButton;
    @FXML private ToggleButtonGroup stateSwitchButton;
    @FXML private ToggleButton autoStateButton;
    @FXML private ToggleButton manualStateButton;
    @FXML private Button resetButton;
    @FXML private TextField messageToEncryptTF;
    @FXML private Label encryptedMessageLabel;
    public MachineEventHandler<EncryptMessageEventListener> activateEncryptEventHandler = new MachineEventHandler<>();

    public void setParentController(BodyController parentController) {

        this.parentController = parentController;
    }

    public void setEncryptedMessageLabel(String encryptedMessage) {

        encryptedMessageLabel.setText(encryptedMessage);
    }

    @FXML
    public void initialize(){

        doneButton.disableProperty().bind(manualStateButton.selectedProperty());
        processButton.disableProperty().bind(autoStateButton.selectedProperty());
        clearButton.disableProperty().bind(autoStateButton.selectedProperty());

    }

    @FXML
    void clearButtonActionListener(ActionEvent event) {

    }

    @FXML
    void doneButtonActionListener(ActionEvent event) {

        activateEncryptEventHandler.fireEvent(messageToEncryptTF.getText());

    }
}
