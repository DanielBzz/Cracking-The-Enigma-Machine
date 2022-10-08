package chat.client.component.chatroom;

import chat.client.component.main.ChatAppMainController;
import components.body.details.MachineConfigurationController;
import components.body.main.EncryptController;
import components.body.main.encryptParentController;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.FlowPane;
import machineDtos.EngineDTO;

public class UBoatRoomContestController implements encryptParentController {

    private UBoatRoomController parentController;

    @FXML
    private MachineConfigurationController machineConfigurationController;
    @FXML
    private EncryptController encryptComponentController;
    @FXML
    private FlowPane activeTeamsDetailsFlowPane;

    @FXML
    private Button readyButton;

    @FXML
    private Button logoutButton;

    @FXML
    private TextArea candidatesArea;

    @FXML
    void logoutButtonListener(ActionEvent event) {

    }

    @FXML
    void readyButtonListener(ActionEvent event) {

    }

    private EngineDTO engine;

    public void initial(){
        if(machineConfigurationController != null){
            machineConfigurationController.setParentController(this);
            machineConfigurationComponent.disableProperty().bind(machineConfigurationComponentController.getIsCodeConfigurationSetProperty().not());
            encryptScreenMachineConfigurationComponentController.bind(machineConfigurationComponentController);
            encryptComponent.disableProperty().bind(machineConfigurationComponentController.getIsCodeConfigurationSetProperty().not());
            encryptScreenMachineConfigurationComponent.disableProperty().bind(machineConfigurationComponent.disableProperty());
        }
        if(encryptComponentController != null) {
            encryptComponentController.setParentController(this);
            encryptComponentController.activateEncryptEventHandler.addListener(mainController.getEncryptMessageEventListener());
            machineConfigurationController.getIsCodeConfigurationSetProperty().addListener(observable -> encryptComponentController.createKeyboards(engineDetails.getEngineComponentsInfo().getABC()));
        }
        if(bruteForceComponentController!= null){
            bruteForceComponentController.setParentController(this);
            bruteForceComponentController.initial();
            bruteForceComponentController.encryptMessageEventHandler().addListener(mainController.getEncryptMessageEventListener());
        }
    }

    public ObjectProperty<EventHandler<ActionEvent>> encryptResetButtonActionProperty(){

        return encryptComponentController.getResetButtonActionProperty();
    }

    public void setUBoatRoomController(UBoatRoomController uBoatRoomController) {
        this.parentController = uBoatRoomController;
    }

    public void addNewTeamDetails(String teamName, int amountOfAgents, int taskSize){
        activeTeamsDetailsFlowPane.getChildren().add(new ActiveTeamDetailsController(teamName, amountOfAgents, taskSize));
    }

    public void setEngine(EngineDTO engine) {
        this.engine = engine;
    }

    public void setIsCodeConfigurationSet(Boolean codeSet){
        machineConfigurationController.getIsCodeConfigurationSetProperty().set(codeSet);
    }

    public void clearDetails(){
        activeTeamsDetailsFlowPane.getChildren().clear();
        candidatesArea.clear();
        machineConfigurationController.clearComponent();
        encryptComponentController.clearButtonActionListener(new ActionEvent());
        encryptComponentController.removeOldAbcFromKeyboards();
    }

    @Override
    public EngineDTO getEngineDetails() {
        return engine;
    }

    //not in use here
    @Override
    public StringProperty getMachineEncryptedMessageProperty() {
        return null;
    }
}
