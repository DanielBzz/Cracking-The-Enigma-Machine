import mainapp.ClientMainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import http.HttpClientUtil;
import util.Constants;

import java.io.IOException;
import java.net.URL;

public class UBoatClient extends Application {

    private ClientMainController uBoatAppMainController;

    @Override
    public void start(Stage primaryStage) {

        primaryStage.setMinHeight(600);
        primaryStage.setMinWidth(600);
        primaryStage.setTitle("UBoat App");

        URL loginPage = ClientMainController.class.getResource("generalMainComponent.fxml");
        try {
            System.out.println(loginPage.toString());

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(loginPage);
            Parent root = fxmlLoader.load();
            uBoatAppMainController = fxmlLoader.getController();
            uBoatAppMainController.loadMainAppForm(Constants.UBOAT_MAIN_APP_FXML_RESOURCE_LOCATION);

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
