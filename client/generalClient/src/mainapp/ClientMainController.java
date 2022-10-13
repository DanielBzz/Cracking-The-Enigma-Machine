package mainapp;

import constants.Constants;
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
import login.Loggable;
import login.LoginController;

import java.io.Closeable;
import java.io.IOException;
import java.net.URL;

public class ClientMainController implements Closeable, Loggable {
        @FXML private Label userGreetingLabel;
    @FXML private AnchorPane mainPanel;
    private GridPane loginComponent;
    private LoginController loginComponentController;
    private Parent appComponent;
    private AppMainController appComponentController;
    private final StringProperty currentUserName;

    public ClientMainController() {

        currentUserName = new SimpleStringProperty("");
    }

    @FXML
    public void initialize() {
        userGreetingLabel.textProperty().bind(Bindings.concat("Hello ", currentUserName));
        loadLoginPage();
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
        //appComponentController.close();
    }

    private void loadLoginPage() {
        URL loginPageUrl = getClass().getResource(Constants.LOGIN_PAGE_FXML_RESOURCE_LOCATION);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(loginPageUrl);
            loginComponent = fxmlLoader.load();
            loginComponentController = fxmlLoader.getController();
            loginComponentController.setUBoatAppMainController(this);
            setMainPanelTo(loginComponent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadMainAppForm(String resourceFromClient) {        // this function not working yet , need to find the path to load

//        //System.out.println(this.getClass().getResource("generalMainComponent.fxml").toString());
//        Path path = Paths.get(resourceFromClient);
//        Path currentDir = Paths.get("client",resourceFromClient);
//        System.out.println(currentDir.toAbsolutePath());
//
//        try {
//            URL loginUrl = currentDir.toAbsolutePath().toUri().toURL();
//            FXMLLoader fxmlLoader = new FXMLLoader();
//            fxmlLoader.setLocation(loginUrl);
//            appComponent = fxmlLoader.load();
//            appComponentController = fxmlLoader.getController();
//            appComponentController.setClientMainController(this);
////            //initial();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        System.out.println("yesss");
    }

    @Override
    public void switchToMainApp() {
        System.out.println("--------------in switch to second room---------");
        setMainPanelTo(appComponent);
        //uBoatRoomComponentController.setActive();
    }

    public void switchToLogin() {       // should activate when press on logout \ the server send redirect to log in
        Platform.runLater(() -> {
            currentUserName.set("");
            //uBoatRoomComponentController.setInActive();
            appComponentController.clearComponent();
            setMainPanelTo(loginComponent);
        });
    }


}