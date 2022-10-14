package components.subControllers;

import components.body.main.BruteForceController;
import components.main.AlliesMainAppController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class AlliesDecryptionProgressAndCandidatesController {

    @FXML
    private ComboBox<?> levelComboBox;

    @FXML
    private Spinner<?> taskSizeSpinner;

    @FXML
    private Label tasksAmountLabel;

    @FXML
    private Button readyButton;

    @FXML
    private Slider agentsNumberSlider;

    @FXML
    private Label agentNumberLabel;

    @FXML
    private TextArea candidatesArea;

    @FXML
    private ProgressBar decryptionProgressBar;

    @FXML
    private Label decryptionProgressLabel;

    private AlliesContestController parentController;

    public void setAlliesContestController(AlliesContestController alliesContestController) {
        this.parentController = alliesContestController;
    }

    @FXML
    void readyButtonOnAction(ActionEvent event) {
        //need to send all the relevant information to the server and to update the uBoat
    }



    public void onTaskFinished(){
        decryptionProgressBar.progressProperty().unbind();
        decryptionProgressLabel.textProperty().unbind();
        readyButton.setDisable(false);
    }

    public void clearController() {
        candidatesArea.setText("");
        //decryptionProgressLabel.setText("0%");
        decryptionProgressBar.setProgress(0);

    }
}
