package components.body.main;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import logic.events.EncryptMessageEventListener;
import logic.events.handler.MachineEventHandler;

public class EncryptController {

    private encryptParentController parentController;
    @FXML private Button processButton;
    @FXML private Button doneButton;
    @FXML private Button clearButton;
    private final ToggleGroup stateSwitchButton = new ToggleGroup();
    @FXML private ToggleButton autoStateButton;
    @FXML private ToggleButton manualStateButton;
    @FXML private Button resetButton;
    @FXML private TextField messageToEncryptTF;
    @FXML private Label encryptedMessageLabel;
    public MachineEventHandler<EncryptMessageEventListener> activateEncryptEventHandler = new MachineEventHandler<>();

    public void setParentController(encryptParentController parentController) {

        this.parentController = parentController;
    }

    public void setEncryptedMessageLabel(String encryptedMessage) {

        encryptedMessageLabel.setText(encryptedMessageLabel.getText() + encryptedMessage);
    }

    public StringProperty getEncryptedMessage(){

        return encryptedMessageLabel.textProperty();
    }

    public ObjectProperty<EventHandler<ActionEvent>> getResetButtonActionProperty(){

        return resetButton.onActionProperty();
    }
    @FXML
    public void initialize(){

        doneButton.disableProperty().bind(manualStateButton.selectedProperty().not());
        processButton.disableProperty().bind(autoStateButton.selectedProperty().not());
        clearButton.disableProperty().bind(autoStateButton.selectedProperty().not());
        autoStateButton.setToggleGroup(stateSwitchButton);
        manualStateButton.setToggleGroup(stateSwitchButton);
    }

    public void setAutoStateOnly(){

        autoStateButton.setSelected(true);
        manualStateButton.setDisable(true);
        stateSwitchButton.getToggles().remove(manualStateButton);
    }

    @FXML
    void processButtonActionListener(ActionEvent event) {

        activateEncryptEventHandler.fireEvent(messageToEncryptTF.getText());
        messageToEncryptTF.setEditable(false);
        // add it to statistics
    }

    @FXML
    void clearButtonActionListener(ActionEvent event) {

        messageToEncryptTF.setText("");
        encryptedMessageLabel.setText("");
        messageToEncryptTF.setEditable(true);
    }

    @FXML
    void doneButtonActionListener(ActionEvent event) {

        messageToEncryptTF.setEditable(false);
        // add it to statistics
    }

    @FXML
    void onKeyTypedActionListener(KeyEvent event) {

        if(!parentController.getEngineDetails().getEngineComponentsInfo().getABC().contains(event.getCharacter().toUpperCase())){
            event.consume();
        }
        else if (manualStateButton.isSelected()) {
            activateEncryptEventHandler.fireEvent(event.getCharacter());
        }
    }

    @FXML
    void onKeyPressedActionListener(KeyEvent event) {

        if(manualStateButton.isSelected() && !event.getCode().isDigitKey()){
            event.consume();
            System.out.println(event.getCode().isDigitKey());
        }
    }

    @FXML
    void stateButtonActionListener(Event event) {

        if(stateSwitchButton.getSelectedToggle() == null){
            ((ToggleButton) event.getSource()).setSelected(true);
        }else {
            clearButtonActionListener(new ActionEvent());
        }
    }

}
