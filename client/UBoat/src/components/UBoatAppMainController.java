package components;

import components.api.HttpStatusUpdate;
import components.subControllers.UBoatRoomController;
import components.login.LoginController;
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
import logic.UBoatLogicUI;
import logic.events.CodeSetEventListener;
import logic.events.EncryptMessageEventListener;
import logic.events.StatisticsUpdateEventListener;
import logic.events.handler.MachineEventHandler;
import machineDtos.EngineDTO;
import machineDtos.MachineInfoDTO;

import java.io.Closeable;
import java.io.IOException;
import java.net.URL;

import static util.Constants.*;

public class UBoatAppMainController implements Closeable, HttpStatusUpdate {
    private UBoatLogicUI uBoatLogicUI;
    @FXML
    private Label userGreetingLabel;
    @FXML
    private AnchorPane mainPanel;
    private GridPane loginComponent;
    private LoginController logicController;
    private Parent uBoatRoomComponent;
    private UBoatRoomController uBoatRoomComponentController;
    private final StringProperty currentUserName;

    public UBoatAppMainController() {
        currentUserName = new SimpleStringProperty();
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
            System.out.println("--------------in load uboat room page---------");
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(loginPageUrl);
            uBoatRoomComponent = fxmlLoader.load();
            uBoatRoomComponentController = fxmlLoader.getController();
            uBoatRoomComponentController.setUBoatAppMainController(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateHttpLine(String line) {
        //httpStatusComponentController.addHttpStatusLine(line);
    }

    public void switchToSecondRoom() {
        //setMainPanelTo(chatRoomComponent);
        System.out.println("--------------in switch to second room---------");
        setMainPanelTo(uBoatRoomComponent);
        //uBoatRoomComponentController.setActive();
    }
    public void switchToLogin() {
        Platform.runLater(() -> {
            currentUserName.set("");
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

    public void setMachineUI(UBoatLogicUI machine){

        uBoatLogicUI = machine;
    }

    public void setMachineSpecification(EngineDTO details){

        uBoatRoomComponentController.setEngineDetails(details);
    }

    public MachineEventHandler<CodeSetEventListener> CodeSetEventHandler()  {

        return uBoatLogicUI.codeSetEventHandler;
    }

    public MachineEventHandler<StatisticsUpdateEventListener> statisticsUpdateEventHandler()  {

        return null; //uBoatLogicUI.statisticsUpdateEventHandler;
    }

    public StringProperty getMachineEncryptedMessageProperty(){

        return uBoatLogicUI.getEncryptedMessageProperty();
    }

    public EncryptMessageEventListener getEncryptMessageEventListener(){
        return new EncryptMessageEventListener() {
            @Override
            public void invoke(String arg) {
                //uBoatLogicUI.encryptInput(arg);
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

    public void manualInitialCodeConfiguration(MachineInfoDTO newValue) throws IOException {
        uBoatLogicUI.manualInitialCodeConfiguration(newValue);
    }

    public void loadNewXmlFile(String newValue){
        uBoatLogicUI.loadNewXmlFile(newValue);
    }

//    public void automaticInitialCodeConfiguration(){
//        uBoatLogicUI.automaticInitialCodeConfiguration();
//    }
}
