package chat.component.main;

import chat.component.api.HttpStatusUpdate;
import chat.component.chatroom.UBoatRoomController;
import chat.component.login.LoginController;
import chat.component.status.StatusController;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import logic.MachineLogicUI;
import logic.events.CodeSetEventListener;
import logic.events.EncryptMessageEventListener;
import logic.events.StatisticsUpdateEventListener;
import logic.events.handler.MachineEventHandler;
import machineDtos.EngineDTO;

import java.io.Closeable;
import java.io.IOException;
import java.net.URL;

import static chat.util.Constants.*;
import static chat.util.Constants.JHON_DOE;

public class UBoatAppMainController implements Closeable, HttpStatusUpdate {

    //new UI

    private MachineLogicUI machineUI;
    @FXML
    private Label userGreetingLabel;

    @FXML
    private AnchorPane mainPanel;


    //@FXML private Parent httpStatusComponent;
//    @FXML private StatusController httpStatusComponentController;

    private GridPane loginComponent;
    private LoginController logicController;

    //private Parent chatRoomComponent;
    private Parent uBoatRoomComponent;

    //private ChatRoomMainController chatRoomComponentController;
    private UBoatRoomController uBoatRoomComponentController;

    private final StringProperty currentUserName;

    public UBoatAppMainController() {
        currentUserName = new SimpleStringProperty(JHON_DOE);
    }

    @FXML
    public void initialize() {
        userGreetingLabel.textProperty().bind(Bindings.concat("Hello ", currentUserName));
        // prepare components
        loadLoginPage();
        loadUBoatRoomPage();
    }

    public void updateUserName(String userName) {
        currentUserName.set(userName);
    }

    private void setMainPanelTo(Parent pane) {
        mainPanel.getChildren().clear();
        mainPanel.getChildren().add(pane);
        AnchorPane.setBottomAnchor(pane, 1.0);
        AnchorPane.setTopAnchor(pane, 1.0);
        AnchorPane.setLeftAnchor(pane, 1.0);
        AnchorPane.setRightAnchor(pane, 1.0);
    }

    @Override
    public void close() throws IOException {
        //chatRoomComponentController.close();
    }

    private void loadLoginPage() {
        URL loginPageUrl = getClass().getResource(LOGIN_PAGE_FXML_RESOURCE_LOCATION);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(loginPageUrl);
            loginComponent = fxmlLoader.load();
            logicController = fxmlLoader.getController();
            logicController.setUBoatAppMainController(this);
            setMainPanelTo(loginComponent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadUBoatRoomPage() {
        URL loginPageUrl = getClass().getResource(/*CHAT_ROOM_FXML_RESOURCE_LOCATION*/UBOAT_ROOM_FXML_RESOURCE_LOCATION);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(loginPageUrl);
            uBoatRoomComponent = fxmlLoader.load();
            uBoatRoomComponentController = fxmlLoader.getController();
            uBoatRoomComponentController.setUBoatAppMainController(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*        take care after deciding if need the status or not         */
    @Override
    public void updateHttpLine(String line) {
        //httpStatusComponentController.addHttpStatusLine(line);
    }

    public void switchToSecondRoom() {
        //setMainPanelTo(chatRoomComponent);
        setMainPanelTo(uBoatRoomComponent);
        //uBoatRoomComponentController.setActive();
    }
    public void switchToLogin() {
        Platform.runLater(() -> {
            currentUserName.set(JHON_DOE);
            //chatRoomComponentController.setInActive();
            //uBoatRoomComponentController.setInActive();
            setMainPanelTo(loginComponent);
        });
    }



    public void initial() {

        if(uBoatRoomComponentController != null){
            uBoatRoomComponentController.initial();
        }
    }

    public void setMachineUI(MachineLogicUI machine){

        machineUI = machine;
    }

    public void setMachineSpecification(EngineDTO details){

        uBoatRoomComponentController.setEngineDetails(details);
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

    public EncryptMessageEventListener getEncryptMessageEventListener(){
        return new EncryptMessageEventListener() {
            @Override
            public void invoke(String arg) {
                machineUI.encryptInput(arg);
            }
        };
    }

    private void clearStage(){

        uBoatRoomComponentController.clearComponent();
    }

    public void setIsGoodFileSelected(boolean fileIsGood){
        uBoatRoomComponentController.setIsGoodFileSelected(fileIsGood);
    }

    public void setSelectedFile(String filePath){
        uBoatRoomComponentController.setSelectedFile(filePath);
    }

    public void showPopUpMessage(String message){
        uBoatRoomComponentController.showPopUpMessage(message);
    }
}
