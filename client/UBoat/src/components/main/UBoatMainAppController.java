package components.main;

import components.body.main.encryptParentController;
import components.header.HeaderController;
import components.subControllers.UBoatRoomContestController;
import components.subControllers.UBoatRoomMachineController;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.GridPane;
import logic.UBoatLogic;
import machineDtos.EngineDTO;
import mainapp.AppMainController;
import mainapp.ClientMainController;

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
            uBoatRoomMachineComponentController.setParentController(this);
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
        uBoatRoomContestComponentController.clearDetails();
        uBoatRoomMachineComponentController.clearDetails();
        //need to take care for the header
    }

    @Override
    public void clearComponent(){

        //bruteForceComponentController.clearComponent();
        //need to find a solution for this ^^^
        //uBoatRoomMachineController.clearDetails();
        //uBoatRoomContestController.clearDetails();
        //bodyComponentController.clearComponent();
    }

    @Override
    public void loadClientMainPage() {
        //parentController.loadMainAppForm(Constants.UBOAT_MAIN_APP_FXML_RESOURCE_LOCATION);
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

                EngineDTO engineDTO = UBoatLogic.uploadFileToServer(selectedFileProperty().get());
                uBoatRoomMachineComponentController.setEngine(engineDTO);
                //uBoatRoomMachineComponentController.initialEngineDetails();

                // need to enable the screen , but first disable it in the initialie.
            }
        });
    }


    public void setIsGoodFileSelected(Boolean isGood) {

        isGoodFileSelected.set(isGood);
    }

    public void showPopUpMessage(String messageToShow){

        new Alert(Alert.AlertType.ERROR, messageToShow, ButtonType.OK).show();
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
