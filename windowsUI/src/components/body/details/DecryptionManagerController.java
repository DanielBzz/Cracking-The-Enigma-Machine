package components.body.details;

import components.body.main.BruteForceController;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import decryptionDtos.AgentAnswerDTO;
import logic.DecryptLogicUI;
import logic.DifficultyLevel;
import decryptionDtos.DecryptionArgumentsDTO;
import manager.DecryptionManager;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class DecryptionManagerController {

    @FXML private ComboBox<String> levelComboBox;
    @FXML private Slider agentsNumberSlider;
    @FXML private Spinner<Integer> taskSizeSpinner;
    @FXML private Label tasksAmountLabel;
    @FXML private Label agentNumberLabel;
    @FXML private Button startButton;
    @FXML private Button stopButton;
    @FXML private Button pauseButton;
    @FXML private Button resumeButton;
    @FXML private ProgressBar decryptionProgressBar;
    @FXML private Label decryptionProgressLabel;
    @FXML private TextArea candidatesArea;
    private BruteForceController parentController;
    DecryptLogicUI decryptLogic = new DecryptLogicUI();

    public void initial(){

        levelComboBox.setItems(FXCollections.observableList(
                Arrays.stream(DifficultyLevel.values()).map(v->(String)v.toString()).collect(Collectors.toList())));
        taskSizeSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1,Integer.MAX_VALUE));
        agentsNumberSlider.setMin(2);
        agentsNumberSlider.setMax(decryptLogic.getMaxAgentTask());
        agentNumberLabel.textProperty().bind(agentsNumberSlider.valueProperty().asString());
        taskSizeSpinner.valueProperty().addListener((
                observable, oldValue, newValue) -> tasksAmountLabel.setText(String.valueOf((int)Math.ceil(decryptLogic.getTaskSize() / (double)newValue))));

        pauseButton.setDisable(true);
        resumeButton.setDisable(true);
        stopButton.disableProperty().bind(startButton.disabledProperty().not());
        startButton.disableProperty().addListener((observable, oldValue, newValue) -> pauseButton.setDisable(oldValue));
        startButton.disableProperty().addListener((observable, oldValue, newValue) -> resumeButton.setDisable(oldValue));

        decryptLogic.setControllerToUpdate(this);
    }

    public void setParentController(BruteForceController parentController) {

        this.parentController = parentController;
    }

    @FXML
    void pauseButtonOnAction(ActionEvent event) {
    }

    @FXML
    void resumeButtonOnAction(ActionEvent event) {

    }

    @FXML
    void startButtonOnAction(ActionEvent event) {

        if(agentsNumberSlider.valueProperty() == null || taskSizeSpinner.valueProperty() == null || levelComboBox.valueProperty() == null){
            new Alert(Alert.AlertType.WARNING, "You did not initial all the arguments", ButtonType.OK).show();
        }
        else{
            Consumer<AgentAnswerDTO> updateCandidates = dto -> {
                StringBuilder answer = new StringBuilder();
                Map<String, List<Character>> candidates = dto.getDecryptedMessagesCandidates();
                candidates.keySet().forEach(candidate -> answer.append(candidate + " Configuration is: " + candidates.get(candidate) + " by: " + dto.getAgentId()));
                candidatesArea.setText(candidatesArea.getText() + System.lineSeparator() + answer.toString());
            };

            DecryptionArgumentsDTO args = new DecryptionArgumentsDTO();
            args.setLevel(DifficultyLevel.valueOf(levelComboBox.getValue()));
            args.setMessageToDecrypt(parentController.getEncryptedMessage());
            args.setTaskSize(taskSizeSpinner.getValue());
            args.setAgentsNumber(agentsNumberSlider.valueProperty().intValue());
            args.setAmountOfTasks(Integer.parseInt(tasksAmountLabel.getText()));
            args.setReflectorId(parentController.getEngineDetails().getMachineInitialInfo().getReflectorID());
            args.setRotorsId(parentController.getEngineDetails().getMachineInitialInfo().getRotorsID());
            startButton.setDisable(true);
            decryptLogic.decryptMessage(updateCandidates,args);
        }
    }

    @FXML
    void stopButtonOnAction(ActionEvent event) {
        decryptLogic.cancelTask();
        startButton.setDisable(false);
    }

    public void setDecryptionManager(DecryptionManager decryptionManager) {
        decryptLogic.setDecryptionManager(decryptionManager);
    }

    public void bindTaskToController(Task task){

        decryptionProgressBar.progressProperty().bind(task.progressProperty());
        decryptionProgressLabel.textProperty().bind( Bindings.concat(
                Bindings.format(
                        "%.0f",
                        Bindings.multiply(
                                task.progressProperty(),
                                100)),
                " %"));
    }
}
