package components.subControllers;

import components.body.details.MachineConfigurationController;
import components.body.main.EncryptController;
import components.body.main.EngineDtoReturnableParentController;
import components.body.main.encryptParentController;
import contestDtos.ActiveTeamDTO;
import decryptionDtos.AgentAnswerDTO;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import machineDtos.EngineDTO;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class UBoatRoomContestController implements encryptParentController, EngineDtoReturnableParentController {

    private UBoatRoomController parentController;

    @FXML
    private MachineConfigurationController machineConfigurationController;
    @FXML
    private BorderPane machineConfigurationComponent;
    @FXML
    private EncryptController encryptComponentController;
    @FXML
    private GridPane encryptComponent;
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

        //add new listener to AnswerToDM
        //fire contest

    }



    public void initial(){
        if(machineConfigurationController != null){
            machineConfigurationController.setParentController(this);
            machineConfigurationComponent.disableProperty().bind(machineConfigurationController.getIsCodeConfigurationSetProperty().not());
            //encryptScreenMachineConfigurationComponentController.bind(machineConfigurationComponentController);
            encryptComponent.disableProperty().bind(machineConfigurationController.getIsCodeConfigurationSetProperty().not());
            //encryptScreenMachineConfigurationComponent.disableProperty().bind(machineConfigurationComponent.disableProperty());
        }
        if(encryptComponentController != null) {
            encryptComponentController.setParentController(this);
            encryptComponentController.activateEncryptEventHandler.addListener(parentController.getEncryptMessageEventListener());
            machineConfigurationController.getIsCodeConfigurationSetProperty().addListener(observable -> encryptComponentController.createKeyboards(parentController.getEngineDetails().getEngineComponentsInfo().getABC()));
        }
        candidatesArea.setEditable(false);
//        if(bruteForceComponentController!= null){
//            bruteForceComponentController.setParentController(this);
//            bruteForceComponentController.initial();
//            bruteForceComponentController.encryptMessageEventHandler().addListener(mainController.getEncryptMessageEventListener());
//        }
    }

    public ObjectProperty<EventHandler<ActionEvent>> encryptResetButtonActionProperty(){

        return encryptComponentController.getResetButtonActionProperty();
    }

    public void setUBoatRoomController(UBoatRoomController uBoatRoomController) {
        this.parentController = uBoatRoomController;
    }

    public void addNewTeamDetails(ActiveTeamDTO newTeam){
        activeTeamsDetailsFlowPane.getChildren().add(new ActiveTeamDetailsComponent(newTeam.getTeamName(), newTeam.getAmountOfAgents(), newTeam.getTaskSize()));
    }

//    public void setEngine(EngineDTO engine) {
//        this.engine = engine;
//    }

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

    public MachineConfigurationController getMachineConfigurationController(){
        return machineConfigurationController;
    }

    public EncryptController getEncryptComponentController(){
        return encryptComponentController;
    }

    @Override
    public EngineDTO getEngineDetails() {
        return null;
    }

    //not in use here
    @Override
    public StringProperty getMachineEncryptedMessageProperty() {
        return null;
    }

    public void updateCandidates(String newCandidates){
        candidatesArea.setText(candidatesArea.getText() + newCandidates);
    }
}
