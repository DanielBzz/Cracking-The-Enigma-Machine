package components;

import components.login.Loggable;
import components.login.LoginController;
import components.subControllers.UBoatRoomController;
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

import static util.Constants.LOGIN_PAGE_FXML_RESOURCE_LOCATION;
import static util.Constants.UBOAT_ROOM_FXML_RESOURCE_LOCATION;

public class UBoatAppMainController implements Closeable, Loggable {
    private UBoatLogicUI uBoatLogicUI;
    @FXML private Label userGreetingLabel;
    @FXML private AnchorPane mainPanel;
    private GridPane loginComponent;
    private LoginController logicController;
    private Parent uBoatRoomComponent;
    private UBoatRoomController uBoatRoomComponentController;
    private final StringProperty currentUserName;

    public UBoatAppMainController() {
        currentUserName = new SimpleStringProperty("");
        uBoatLogicUI = new UBoatLogicUI(this);
    }

    @FXML
    public void initialize() {
        userGreetingLabel.textProperty().bind(Bindings.concat("Hello ", currentUserName));
       loadLoginPage();
       loadUBoatRoomPage();
    }

    @Override
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
        uBoatRoomComponentController.close();
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
        URL loginPageUrl = getClass().getResource(UBOAT_ROOM_FXML_RESOURCE_LOCATION);
        try {
            System.out.println("--------------in load uboat room page---------");
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(loginPageUrl);
            uBoatRoomComponent = fxmlLoader.load();
            uBoatRoomComponentController = fxmlLoader.getController();
            System.out.println("HeaderComponentController:" + uBoatRoomComponentController.getHeaderComponentController());
            System.out.println("UBoatRoomMachineController:" + uBoatRoomComponentController.getUBoatRoomMachineController());
            //System.out.println(uBoatRoomComponentController);
            uBoatRoomComponentController.setUBoatAppMainController(this);
            //initial();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void switchToMainApp() {
        System.out.println("--------------in switch to second room---------");
        setMainPanelTo(uBoatRoomComponent);
        //uBoatRoomComponentController.setActive();
    }

    public void switchToLogin() {       // should activate when press on logout \ the server send redirect to log in
        Platform.runLater(() -> {
            currentUserName.set("");
            //uBoatRoomComponentController.setInActive();
            uBoatRoomComponentController.clearComponent();
            setMainPanelTo(loginComponent);
        });
    }

    public void initial() {
        System.out.println("uBoatRoomComponentController:" + uBoatRoomComponentController);
        if(uBoatRoomComponentController != null){
            uBoatRoomComponentController.initial();
        }
    }



    // from here all the function should be in internal classes and not here
    // this class should be only manage of the component and controller that now control the app, login/uBoatRoom


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
                uBoatLogicUI.encryptInput(arg);
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

    public void loadNewXmlFile(String newValue){
        uBoatLogicUI.loadNewXmlFile(newValue);
    }

    public void manualInitialCodeConfiguration(MachineInfoDTO newValue) throws IOException {
        uBoatLogicUI.manualInitialCodeConfiguration(newValue);
    }


//    public void automaticInitialCodeConfiguration(){
//        uBoatLogicUI.automaticInitialCodeConfiguration();
//    }
}
