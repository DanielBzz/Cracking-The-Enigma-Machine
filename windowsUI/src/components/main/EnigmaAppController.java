package components.main;

import components.body.main.BodyController;
import components.header.HeaderController;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TabPane;
import logic.EnigmaMachineUI;
import machineDtos.EngineInfoDTO;

public class EnigmaAppController {

    private EnigmaMachineUI machineUI;
    @FXML
    private ScrollPane headerComponent;
    @FXML
    private HeaderController headerComponentController;
    @FXML
    private TabPane bodyComponent;
    @FXML
    private BodyController bodyComponentController;
    private final SimpleStringProperty selectedFileProperty = new SimpleStringProperty("-");
    private final SimpleBooleanProperty isFileSelected = new SimpleBooleanProperty(false);

    @FXML
    public void initialize() throws Exception {

        if (headerComponentController != null && bodyComponentController != null) {
            headerComponentController.setMainController(this);
            bodyComponentController.setMainController(this);
            headerComponentController.initial();
            bodyComponentController.initial();
            bodyComponent.disableProperty().bind(isFileSelected.not());
        }

        isFileSelected.bind(selectedFileProperty.isNotEqualTo("-"));
        selectedFileProperty.addListener((observable, oldValue, newValue) -> machineUI.loadNewXmlFile());
        selectedFileProperty.addListener((observable, oldValue, newValue) -> machineUI.displayingMachineSpecification());
    }

    public void setMachineLogic(EnigmaMachineUI machine){

        machineUI = machine;
    }

    public SimpleStringProperty selectedFileProperty() {

        return this.selectedFileProperty;
    }

    public void setSelectedFile(String selectedFilePath) {

        selectedFileProperty.set(selectedFilePath);
    }

    public String getSelectedFile(){

        return selectedFileProperty.get();
    }

    public void showPopUpMessage(String messageToShow){

        new Alert(Alert.AlertType.ERROR, messageToShow, ButtonType.OK).show();
    }

    public void setMachineSpecification(EngineInfoDTO details){

        bodyComponentController.setEngineDetails(details);
    }




}
