package components.subControllers;

import components.CandidatesTableController;
import components.body.main.BruteForceController;
import components.main.AlliesMainAppController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.util.StringConverter;

public class AlliesDecryptionProgressAndCandidatesController {

    @FXML
    private Label levelLabel;

    @FXML
    private Spinner<Integer> taskSizeSpinner;

    @FXML
    private Label tasksAmountLabel;

    @FXML
    private Button readyButton;

    @FXML
    private Slider agentsNumberSlider;

    @FXML
    private Label agentNumberLabel;

    @FXML
    private AnchorPane candidatesTableComponent;
    @FXML
    private CandidatesTableController candidatesTableController;

    @FXML
    private ProgressBar decryptionProgressBar;

    @FXML
    private Label decryptionProgressLabel;
    @FXML
    private Label encryptedMessageLabel;

    private AlliesContestController parentController;

    public void setAlliesContestController(AlliesContestController alliesContestController) {
        this.parentController = alliesContestController;
    }

    @FXML
    void readyButtonOnAction(ActionEvent event) {
        //need to send all the relevant information to the server and to update the uBoat
        readyButton.setDisable(true);
    }

    public void initial(){
        agentsNumberSlider.setMin(2);
        agentNumberLabel.textProperty().bind(agentsNumberSlider.valueProperty().asString());
        initialTaskSpinner();
        readyButton.setDisable(false);
    }

    private void initialTaskSpinner(){

        taskSizeSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1,Integer.MAX_VALUE));
        taskSizeSpinner.valueProperty().addListener((
                observable, oldValue, newValue) -> tasksAmountLabel.setText(String.valueOf((int)Math.ceil(parentController.getTaskSize() / (double)newValue))));
        taskSizeSpinner.getValueFactory().setConverter(new StringConverter<Integer>() {
            @Override
            public String toString(Integer object) {return object.toString();}
            @Override
            public Integer fromString(String string) {
                try {
                    return Integer.parseInt(string);
                }catch (NumberFormatException e){
                    return 2;
                }
            }
        });
        taskSizeSpinner.getValueFactory().setValue(2);
    }

    public void updateEncryptedMessage(String encryptedString){
        encryptedMessageLabel.setText(encryptedString);
    }

    public void updateLevel(String level){
        levelLabel.setText(level);
    }

    public void updateAmountOfAgents(int amountOfAgents){
        agentsNumberSlider.setMax(amountOfAgents);
    }

    public void onTaskFinished(){
        decryptionProgressBar.progressProperty().unbind();
        decryptionProgressLabel.textProperty().unbind();
        readyButton.setDisable(false);
    }

    public void clearController() {
        candidatesTableController.clear();
        //decryptionProgressLabel.setText("0%");
        decryptionProgressBar.setProgress(0);

    }
}
