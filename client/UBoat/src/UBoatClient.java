import http.HttpClientUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import mainapp.ClientMainController;
import util.Constants;

import java.io.IOException;
import java.net.URL;

import static constants.Constants.UBOAT_HEADER;

public class UBoatClient extends Application {

    private ClientMainController uBoatAppMainController;

    @Override
    public void start(Stage primaryStage) {

        //primaryStage.setMinHeight(600);
        //primaryStage.setMinWidth(600);
        primaryStage.setTitle("UBoat App");

        URL clientMainApp = ClientMainController.class.getResource("generalMainComponent.fxml");
        try {

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(clientMainApp);
            Parent root = fxmlLoader.load();
            uBoatAppMainController = fxmlLoader.getController();
            uBoatAppMainController.loadMainAppForm(ClassLoader.getSystemResource(Constants.UBOAT_MAIN_APP_FXML_RESOURCE_LOCATION),Constants.UBOAT_CLIENT);
            uBoatAppMainController.setUserType(UBOAT_HEADER);
            Scene scene = new Scene(root, 700, 600);
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void stop() throws Exception {
        HttpClientUtil.shutdown();
        uBoatAppMainController.close();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
