package components.main;

import components.body.main.BodyController;
import components.header.HeaderController;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TabPane;
import logic.EnigmaMachineUI;

public class EnigmaAppController {

    EnigmaMachineUI machineUI;
    @FXML
    private ScrollPane headerComponent;
    @FXML
    private HeaderController headerComponentController;
    @FXML
    private TabPane bodyComponent;
    @FXML
    private BodyController bodyComponentController;
    private final SimpleStringProperty selectedFileProperty = new SimpleStringProperty();

    @FXML
    public void initialize() {
        if (this.headerComponentController != null && this.bodyComponentController != null) {
            this.headerComponentController.setMainController(this);
            this.bodyComponentController.setMainController(this);
            this.headerComponentController.initial();
        }
    }

    public void setMachineLogic(EnigmaMachineUI machine){
        machineUI = machine;
    }

    public SimpleStringProperty selectedFilePropertyProperty() {

        return this.selectedFileProperty;
    }

    public void setSelectedFileProperty(String selectedFilePath) {

        selectedFileProperty.set(selectedFilePath);

        try {
            machineUI.loadNewXmlFile();
        } catch (Exception | Error e) {
            selectedFileProperty.set("-");
        }
    }
}
