
package components.header;

import components.main.EnigmaAppController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;

public class HeaderController {
    EnigmaAppController mainController;
    @FXML
    private Button loadFileButton;
    @FXML
    private Label filePathLabel;

    public HeaderController() {
        System.out.println("ctor header");
    }

    public void setMainController(EnigmaAppController appController) {
        this.mainController = appController;
    }

    public void initial() {
        this.filePathLabel.textProperty().bind(this.mainController.selectedFilePropertyProperty());
    }

    @FXML
    public void loadFileActionListener(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("load xml file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("xml files", new String[]{"*.xml"}));
        File chosenFile = fileChooser.showOpenDialog((Window)null);
        if (chosenFile != null) {
            this.mainController.setSelectedFileProperty(chosenFile.getAbsolutePath());
        }

    }
}
