package components.subControllers;

import components.UBoatAppMainController;
import components.body.main.encryptParentController;
import components.header.HeaderController;
import components.main.FileLoadable;
import javafx.beans.property.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import machineDtos.EngineDTO;
import machineDtos.MachineInfoDTO;

import java.io.IOException;

public class UBoatRoomController implements FileLoadable, encryptParentController {

    private UBoatAppMainController parentController;
    @FXML
    private UBoatRoomMachineController uBoatRoomMachineController;
    @FXML
    private GridPane uBoatRoomMachineComponent;
//    @FXML
//    private UBoatRoomContestController uBoatRoomContestController;
//    @FXML
//    private GridPane uBoatRoomContestComponent;
    @FXML private HeaderController headerComponentController;
    @FXML private ScrollPane headerComponent;
    @FXML
    private Tab contestTab;
    private final SimpleStringProperty selectedFileProperty = new SimpleStringProperty("-");
    private final SimpleBooleanProperty isGoodFileSelected = new SimpleBooleanProperty(false);
    private EngineDTO engineDetails;

    public void setUBoatAppMainController(UBoatAppMainController uBoatAppMainController) {
        this.parentController = uBoatAppMainController;
    }

    @Override
    public SimpleStringProperty selectedFileProperty() {

        return this.selectedFileProperty;
    }
    @Override
    public void setSelectedFile(String selectedFilePath) {

        selectedFileProperty.set(selectedFilePath);
    }
    public void setIsGoodFileSelected(Boolean isGood) {

        isGoodFileSelected.set(isGood);
    }

    public void showPopUpMessage(String messageToShow){

        new Alert(Alert.AlertType.ERROR, messageToShow, ButtonType.OK).show();
    }

    public void clearComponent(){

        //bruteForceComponentController.clearComponent();
        //need to find a solution for this ^^^
        uBoatRoomMachineController.clearDetails();
        //uBoatRoomContestController.clearDetails();
        //bodyComponentController.clearComponent();
    }

    public void initial() {
        if (headerComponentController != null && uBoatRoomMachineController != null /*&& uBoatRoomContestController != null*/) {
            headerComponentController.setMainController(this);
            headerComponentController.initial();
            uBoatRoomMachineController.setUBoatRoomController(this);
            uBoatRoomMachineController.initial();

            /*            take care of it when fixing the contest screen          */
            //uBoatRoomContestController.setUBoatRoomController(this);
            //uBoatRoomContestController.initial();

            initialFileSelectedEvents();
            initialControllerEventsToLogic();
        }
        setLogicEventsToController();
    }

    private void initialFileSelectedEvents(){

        uBoatRoomMachineComponent.disableProperty().bind(isGoodFileSelected.not());
        //uBoatRoomContestComponent.disableProperty().bind(isGoodFileSelected.not());
        selectedFileProperty.addListener((observable, oldValue, newValue) -> {
            isGoodFileSelected.set(false);
            clearComponent();
            //use the load file servlet
            parentController.loadNewXmlFile(newValue);
        });

        isGoodFileSelected.addListener((observable, oldValue, newValue) -> {
            if(newValue){
                //use the set machine specification servlet
                //machineUI.displayingMachineSpecification();

                uBoatRoomMachineController.initialCodeCalibration();
                uBoatRoomMachineController.initialEngineDetails();

                /*          take care of the contest initial things after fixing the screen       */
                //bodyComponentController.setDecryptionManager(machineUI.getMachineEngine());
                //bodyComponentController.setIsCodeConfigurationSet(false);
            }
        });
    }

    private void initialControllerEventsToLogic(){
        /*          take care after fixing UI       */

        getMachineInfoProperty().addListener(
                (observable, oldValue, newValue) -> {
                    try {
                        parentController.manualInitialCodeConfiguration(newValue);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });

        //uBoatRoomMachineController.codeCalibrationRandomCodeOnAction().set(observable -> machineUI.automaticInitialCodeConfiguration());
        //uBoatRoomContestController.encryptResetButtonActionProperty().set(observable -> machineUI.resetCurrentCode());
    }


    @Override
    public EngineDTO getEngineDetails() {
        return engineDetails;
    }


    public void setEngineDetails(EngineDTO details) {

        engineDetails = details;
    }

    public ObjectProperty<EventHandler<ActionEvent>> codeCalibrationRandomCodeOnAction(){
        return uBoatRoomMachineController.codeCalibrationRandomCodeOnAction();
    }

    public SimpleObjectProperty<MachineInfoDTO> getMachineInfoProperty(){
        return uBoatRoomMachineController.getMachineInfoProperty();
    }


    public void initialCodeCalibration(){
        uBoatRoomMachineController.initialCodeCalibration();
    }

    public void initialEngineDetails(){
        uBoatRoomMachineController.initialEngineDetails();
    }

    //------take care after handling with contest screen------

//    public ObjectProperty<EventHandler<ActionEvent>> encryptResetButtonActionProperty(){
//
//        return encryptComponentController.getResetButtonActionProperty();
//    }


//    public void setDecryptionManager(EnigmaSystemEngine engine){
//        bruteForceComponentController.setDecryptionManager(new DecryptionManager(engine));
//    }

//    public void setIsCodeConfigurationSet(Boolean codeSet){
//        machineConfigurationComponentController.getIsCodeConfigurationSetProperty().set(codeSet);
//    }

//    public SimpleBooleanProperty getIsCodeConfigurationSetProperty(){
//        return machineConfigurationComponentController.getIsCodeConfigurationSetProperty();
//    }


    public StringProperty getMachineEncryptedMessageProperty(){
        return parentController.getMachineEncryptedMessageProperty();
    }

    private void setLogicEventsToController(){

        //mainController.statisticsUpdateEventHandler().addListener(statisticsComponentController);
        parentController.getMachineEncryptedMessageProperty().addListener(
                (observable, oldValue, newValue) -> {

                    //------take care after handling with contest screen------

//                    if(contestTab.isSelected()) {
//                        encryptComponentController.setEncryptedMessageLabel(newValue);
//                    } else {
//                        bruteForceComponentController.setEncryptedMessageLabel(newValue);
//                    }
                });
        //mainController.CodeSetEventHandler().addListener(bruteForceComponentController);

        parentController.CodeSetEventHandler().addListener(uBoatRoomMachineController.getCodeCalibrationComponentController());

        //------take care after handling with contest screen------
        //mainController.CodeSetEventHandler().addListener(machineConfigurationComponentController);

    }
}
