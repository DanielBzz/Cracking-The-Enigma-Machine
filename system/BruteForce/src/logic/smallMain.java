package logic;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class smallMain extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        Spinner<Integer> t = new Spinner<>();
        SpinnerValueFactory d = new SpinnerValueFactory.IntegerSpinnerValueFactory(1,10);
        t.setValueFactory(d);
        AnchorPane a = new AnchorPane(t);
        primaryStage.setTitle("Cracking The Enigma");
        Scene scene = new Scene(a, 1400.0, 900.0);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}