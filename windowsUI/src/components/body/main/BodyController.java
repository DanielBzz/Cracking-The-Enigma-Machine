package components.body.main;

import components.main.EnigmaAppController;
import javafx.fxml.FXML;

public class BodyController {
    private EnigmaAppController mainController;

    public BodyController() {
        System.out.println("ctor body");
    }

    @FXML
    public void initialize() {
        System.out.println("body");
    }

    public void setMainController(EnigmaAppController appController) {
        this.mainController = appController;
    }
}

