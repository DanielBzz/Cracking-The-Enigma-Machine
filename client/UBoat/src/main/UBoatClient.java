package main;

import componets.UBoatAppMainController;
import util.http.HttpClientUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

import static util.Constants.MAIN_PAGE_FXML_RESOURCE_LOCATION;

public class UBoatClient extends Application {

    //private ChatAppMainController chatAppMainController;

    //only for temp time

    private UBoatAppMainController uBoatAppMainController;

    @Override
    public void start(Stage primaryStage) {

        primaryStage.setMinHeight(600);
        primaryStage.setMinWidth(600);
        primaryStage.setTitle("UBoat");

        URL loginPage = getClass().getResource(MAIN_PAGE_FXML_RESOURCE_LOCATION);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(loginPage);
            Parent root = fxmlLoader.load();
            //chatAppMainController = fxmlLoader.getController();
            uBoatAppMainController = fxmlLoader.getController();

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
        //chatAppMainController.close();
        //only for temp time
        uBoatAppMainController.close();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
