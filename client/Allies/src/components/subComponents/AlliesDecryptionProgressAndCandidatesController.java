package components.subComponents;

import com.sun.istack.internal.NotNull;
import components.CandidatesTableController;
import http.HttpClientUtil;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.util.StringConverter;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;

import java.io.IOException;

import static util.Constants.REQUEST_PATH_SET_READY;

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
    private BooleanProperty inContest = new SimpleBooleanProperty(false);

    public void setAlliesContestController(AlliesContestController alliesContestController) {
        this.parentController = alliesContestController;
    }

    public String getContestManagerName(){
        return parentController.getContestName();
    }

    @FXML
    void readyButtonOnAction(ActionEvent event) {
        //need to send all the relevant information to the server and to update the uBoat
        //not finished yet

        String finalUrl = HttpUrl
                .parse(REQUEST_PATH_SET_READY)
                .newBuilder()
                .addQueryParameter("contestManager", getContestManagerName())
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("Could not response well");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (!response.isSuccessful()) {
                    System.out.println("Could not response well, url:" + finalUrl);
                }
                //add the competitors
                System.out.println("Allies was added successfully!");
            }
        });
    }

    public void initial(){
        agentsNumberSlider.setMin(2);
        agentNumberLabel.textProperty().bind(agentsNumberSlider.valueProperty().asString());
        initialTaskSpinner();
        disableBinding();
    }

    public void disableBinding(){
        taskSizeSpinner.disableProperty().bind(inContest);
        agentsNumberSlider.disableProperty().bind(inContest);
        readyButton.disableProperty().bind(inContest);
    }

    public void inFinishedContest(){

        inContest.setValue(false);
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
