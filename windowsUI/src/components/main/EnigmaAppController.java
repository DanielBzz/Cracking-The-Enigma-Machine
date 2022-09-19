package components.main;

import components.body.main.BodyController;
import components.header.HeaderController;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TabPane;
import logic.MachineLogicUI;
import logic.events.CodeSetEventListener;
import logic.events.EncryptMessageEventListener;
import logic.events.StatisticsUpdateEventListener;
import logic.events.handler.MachineEventHandler;
import machineDtos.EngineDTO;

public class EnigmaAppController {

    private MachineLogicUI machineUI;
    @FXML
    private ScrollPane headerComponent;
    @FXML
    private HeaderController headerComponentController;
    @FXML
    private TabPane bodyComponent;
    @FXML
    private BodyController bodyComponentController;
    private final SimpleStringProperty selectedFileProperty = new SimpleStringProperty("-");
    //private final SimpleBooleanProperty isFileSelected = new SimpleBooleanProperty(false);
    private final SimpleBooleanProperty isGoodFileSelected = new SimpleBooleanProperty(false);

    public void initial() {

        if (headerComponentController != null && bodyComponentController != null) {
            headerComponentController.setMainController(this);
            bodyComponentController.setMainController(this);
            headerComponentController.initial();
            bodyComponent.disableProperty().bind(isGoodFileSelected.not());
            bodyComponentController.getMachineInfoProperty().addListener(
                    (observable, oldValue, newValue) -> machineUI.manualInitialCodeConfiguration(newValue));

            //isFileSelected.bind(selectedFileProperty.isNotEqualTo("-"));
            selectedFileProperty.addListener((observable, oldValue, newValue) -> {
                isGoodFileSelected.set(false);
                clearStage();
                machineUI.loadNewXmlFile(newValue);
            });

            isGoodFileSelected.addListener((observable, oldValue, newValue) -> {
                if(newValue){
                    machineUI.displayingMachineSpecification();
                    bodyComponentController.initialCodeCalibration();
                    bodyComponentController.initialEngineDetails();
                    bodyComponentController.setDecryptionManager(machineUI.getMachineEngine());
                    bodyComponentController.setIsCodeConfigurationSet(false);
                }
            });

            bodyComponentController.setListenerToHistoryUpdate(machineUI);
            bodyComponentController.codeCalibrationRandomCodeOnAction().set(observable -> machineUI.automaticInitialCodeConfiguration());
            bodyComponentController.encryptResetButtonActionProperty().set(observable -> machineUI.resetCurrentCode());
            bodyComponentController.initial();

        }
    }

    public void setMachineUI(MachineLogicUI machine){
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

    public void setMachineSpecification(EngineDTO details){

        bodyComponentController.setEngineDetails(details);
    }

    public MachineEventHandler<CodeSetEventListener> CodeSetEventHandler()  {

        return machineUI.codeSetEventHandler;
    }

    public MachineEventHandler<StatisticsUpdateEventListener> statisticsUpdateEventHandler()  {

        return machineUI.statisticsUpdateEventHandler;
    }

    public StringProperty getMachineEncryptedMessageProperty(){

        return machineUI.getEncryptedMessageProperty();
    }

    public void setIsGoodFileSelected(Boolean isGood) {
        isGoodFileSelected.set(isGood);
    }

    public EncryptMessageEventListener getEncryptMessageEventListener(){
        return new EncryptMessageEventListener() {
            @Override
            public void invoke(String arg) {
                machineUI.encryptInput(arg);
            }
        };
    }

    public void clearStage(){
        bodyComponentController.clearComponent();
    }
}
