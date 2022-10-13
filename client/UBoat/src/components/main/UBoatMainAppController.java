package components.main;

import components.body.main.encryptParentController;
import components.header.HeaderController;
import components.subControllers.UBoatRoomContestController;
import components.subControllers.UBoatRoomMachineController;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.GridPane;
import machineDtos.EngineDTO;
import machineDtos.MachineInfoDTO;
import mainapp.AppMainController;
import mainapp.ClientMainController;
import util.Constants;

public class UBoatMainAppController extends FileLoadable implements AppMainController, encryptParentController {

    private ClientMainController parentController;
    @FXML private GridPane uBoatRoomMachineComponent;
    @FXML private UBoatRoomMachineController uBoatRoomMachineComponentController;
    @FXML private GridPane uBoatRoomContestComponent;
    @FXML private UBoatRoomContestController uBoatRoomContestComponentController;
    @FXML private ScrollPane headerComponent;
    @FXML private HeaderController headerComponentController;
    @FXML private Tab contestTab;
    private final SimpleBooleanProperty isGoodFileSelected = new SimpleBooleanProperty(false);
    private EngineDTO engineDetails;

    @FXML
    public void initialize() {
        System.out.println("headerController:" + headerComponentController);
        System.out.println("uBoatRoomMachineController:" + uBoatRoomMachineComponentController);

        if (headerComponentController != null && uBoatRoomMachineComponentController != null && uBoatRoomContestComponentController != null) {
            headerComponentController.setMainController(this);
            headerComponentController.initial();
            uBoatRoomMachineComponentController.setUBoatRoomController(this);
            uBoatRoomMachineComponentController.initial();
            uBoatRoomContestComponentController.setUBoatRoomController(this);
            uBoatRoomContestComponentController.initial();

            initialFileSelectedEvents();
        }
    }

    //---------------------from AppMainController ----------------------------
    @Override
    public void setClientMainController(ClientMainController clientMainController) {
        this.parentController = clientMainController;
    }

    @Override
    public void close(){
        clearComponent();
        parentController.switchToLogin();
    }

    @Override
    public void clearComponent(){
        uBoatRoomContestComponentController.clearDetails();
        uBoatRoomMachineComponentController.clearDetails();
        //need to take care for the header
    }

    @Override
    public void loadClientMainPage() {
        parentController.loadMainAppForm(Constants.UBOAT_MAIN_APP_FXML_RESOURCE_LOCATION);
    }
    //------------------------------------------- ----------------------------

    //---------------------from encryptParent ----------------------------

    @Override
    public EngineDTO getEngineDetails() {
        return engineDetails;
    }

    @Override
    public StringProperty getMachineEncryptedMessageProperty() {        // should return the encrypted message
        return null;
    }

    //------------------------------------------- ----------------------------

    private void initialFileSelectedEvents(){

        uBoatRoomMachineComponent.disableProperty().bind(isGoodFileSelected.not());
        uBoatRoomContestComponent.disableProperty().bind(isGoodFileSelected.not());
        selectedFileProperty().addListener((observable, oldValue, newValue) -> {
            isGoodFileSelected.set(false);
            clearComponent();
            //use the load file servlet             --> should be here call function to server to load file
            // parentController.loadNewXmlFile(newValue);
        });

        isGoodFileSelected.addListener((observable, oldValue, newValue) -> {
            if(newValue){
                //use the set machine specification servlet     // should initial the machine show outside
                //machineUI.displayingMachineSpecification();

                uBoatRoomMachineComponentController.initialCodeCalibration();
                uBoatRoomMachineComponentController.initialEngineDetails();

                /*          take care of the contest initial things after fixing the screen       */
                //bodyComponentController.setDecryptionManager(machineUI.getMachineEngine());
                setIsCodeConfigurationSet(false);
            }
        });
    }









    public void setIsGoodFileSelected(Boolean isGood) {

        isGoodFileSelected.set(isGood);
    }

    public void showPopUpMessage(String messageToShow){

        new Alert(Alert.AlertType.ERROR, messageToShow, ButtonType.OK).show();
    }








    public void setEngineDetails(EngineDTO details) {

        engineDetails = details;
    }

    public ObjectProperty<EventHandler<ActionEvent>> codeCalibrationRandomCodeOnAction(){
        return uBoatRoomMachineComponentController.codeCalibrationRandomCodeOnAction();
    }

    public SimpleObjectProperty<MachineInfoDTO> getMachineInfoProperty(){
        return uBoatRoomMachineComponentController.getMachineInfoProperty();
    }


    public void initialCodeCalibration(){
        uBoatRoomMachineComponentController.initialCodeCalibration();
    }

    public void initialEngineDetails(){
        uBoatRoomMachineComponentController.initialEngineDetails();
    }

    public ObjectProperty<EventHandler<ActionEvent>> encryptResetButtonActionProperty(){

        return uBoatRoomContestComponentController.getEncryptComponentController().getResetButtonActionProperty();
    }

    public void setIsCodeConfigurationSet(Boolean codeSet){
        uBoatRoomContestComponentController.getMachineConfigurationController().getIsCodeConfigurationSetProperty().set(codeSet);
    }

    public SimpleBooleanProperty getIsCodeConfigurationSetProperty(){
        return uBoatRoomContestComponentController.getMachineConfigurationController().getIsCodeConfigurationSetProperty();
    }


}
