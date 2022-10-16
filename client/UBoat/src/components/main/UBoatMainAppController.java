package components.main;

import components.header.HeaderController;
import components.subControllers.UBoatRoomContestController;
import components.subControllers.UBoatRoomMachineController;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.GridPane;
import logic.UBoatLogic;
import logic.events.CodeSetEventListener;
import machineDtos.EngineDTO;
import machineDtos.MachineInfoDTO;
import mainapp.AppMainController;
import mainapp.ClientMainController;

public class UBoatMainAppController extends FileLoadable implements AppMainController {

    private ClientMainController parentController;
    @FXML private GridPane uBoatRoomMachineComponent;
    @FXML private UBoatRoomMachineController uBoatRoomMachineComponentController;
    @FXML private GridPane uBoatRoomContestComponent;
    @FXML private UBoatRoomContestController uBoatRoomContestComponentController;
    @FXML private ScrollPane headerComponent;
    @FXML private HeaderController headerComponentController;
    @FXML private Tab contestTab;
    private EngineDTO engineDetails;
    private UBoatLogic uBoatLogic;

    @FXML
    public void initialize() {

        uBoatLogic = new UBoatLogic(this);
        if (headerComponentController != null && uBoatRoomMachineComponentController != null && uBoatRoomContestComponentController != null) {
            headerComponentController.setMainController(this);
            headerComponentController.initial();
            uBoatRoomMachineComponentController.setParentController(this);
            uBoatRoomMachineComponentController.initial();
            uBoatRoomContestComponentController.setUBoatRoomController(this);

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
        //parentController.loadMainAppForm(getClass().getResource(Constants.UBOAT_MAIN_APP_FXML_RESOURCE_LOCATION));
    }

    //------------------------------------------- ----------------------------

    public EngineDTO getEngineDetails() {
        return engineDetails;
    }

    public void setEngineDetails(EngineDTO engine){
        this.engineDetails = engine;
    }

    public void initialFileSelectedEvents(){

        uBoatRoomMachineComponent.disableProperty().bind(isGoodFileSelectedProperty().not());
        uBoatRoomContestComponent.disableProperty().bind(isGoodFileSelectedProperty().not());
        selectedFileProperty().addListener((observable, oldValue, newValue) -> {
            setIsGoodFileSelected(false);
            clearComponent();
            uBoatLogic.uploadFileToServer(selectedFileProperty().get());
        });

        isGoodFileSelectedProperty().addListener((observable, oldValue, newValue) -> {

            if(newValue){

                uBoatRoomMachineComponentController.setEngineDetailsInComponents(engineDetails);
            }
        });
    }

    public void initialMachineConfiguration(MachineInfoDTO machineInfo){
        uBoatLogic.updateMachineConfiguration(machineInfo);
    }

    public void showPopUpMessage(String messageToShow){

        new Alert(Alert.AlertType.ERROR, messageToShow, ButtonType.OK).show();
    }

    public void addListenerForCodeSet(CodeSetEventListener listener){

        uBoatLogic.codeSetEventHandler.addListener(listener);
    }







//    public ObjectProperty<EventHandler<ActionEvent>> encryptResetButtonActionProperty(){
//
//        return uBoatRoomContestComponentController.getEncryptComponentController().getResetButtonActionProperty();
//    }
//
//    public void setIsCodeConfigurationSet(Boolean codeSet){
//        uBoatRoomContestComponentController.getMachineConfigurationController().getIsCodeConfigurationSetProperty().set(codeSet);
//    }
//
//    public SimpleBooleanProperty getIsCodeConfigurationSetProperty(){
//        return uBoatRoomContestComponentController.getMachineConfigurationController().getIsCodeConfigurationSetProperty();
//    }

}
