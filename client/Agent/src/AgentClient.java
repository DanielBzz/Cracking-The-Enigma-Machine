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

public class AgentClient extends Application {

    ClientMainController mainAppController;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Agent App");
        URL clientMainApp = ClientMainController.class.getResource("generalMainComponent.fxml");

        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(clientMainApp);
            Parent root = fxmlLoader.load();
            mainAppController = fxmlLoader.getController();
            mainAppController.loadMainAppForm(getClass().getResource(Constants.AGENT_ENTER_DETAILS_FXML_RESOURCE_LOCATION),Constants.AGENT_CLIENT);

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
        mainAppController.close();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
