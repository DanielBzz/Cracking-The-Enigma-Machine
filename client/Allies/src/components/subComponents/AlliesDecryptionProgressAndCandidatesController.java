package components.subComponents;

import com.sun.istack.internal.NotNull;
import components.CandidatesTableController;
import components.DynamicComponent;
import contestDtos.ContestDetailsDTO;
import http.HttpClientUtil;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.util.StringConverter;
import okhttp3.*;

import java.io.IOException;
import java.util.function.Consumer;

import static util.Constants.REQUEST_PATH_SET_READY;

public class AlliesDecryptionProgressAndCandidatesController {

    private AlliesContestController parentController;
    @FXML private Label levelLabel;
    @FXML private Spinner<Integer> taskSizeSpinner;
    @FXML private Label tasksAmountLabel;
    @FXML private Button readyButton;
    @FXML private AnchorPane candidatesPlace;
    private CandidatesTableController candidatesTableController;
    @FXML private ProgressBar decryptionProgressBar;
    @FXML private Label decryptionProgressLabel;
    @FXML private Label encryptedMessageLabel;
    private final BooleanProperty inContest = new SimpleBooleanProperty(false);

    public void initial(ContestDetailsDTO contestDetails){
        initialTaskSpinner(contestDetails.getTaskSize());
        disableBinding();
        encryptedMessageLabel.setText(contestDetails.getEncryptedMessage());
        levelLabel.setText(contestDetails.getLevel());
        try {
            FXMLLoader load = new FXMLLoader();
            load.setLocation(CandidatesTableController.class.getResource("candidates-table.fxml"));
            Node newComponent = load.load();
            candidatesPlace.getChildren().add(newComponent);
            DynamicComponent.fitToPane(newComponent);
            candidatesTableController = load.getController();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    public void setAlliesContestController(AlliesContestController alliesContestController) {
        this.parentController = alliesContestController;
    }

    @FXML
    void readyButtonOnAction(ActionEvent event) {

        String finalUrl = HttpUrl
                .parse(REQUEST_PATH_SET_READY)
                .newBuilder()
                .addQueryParameter("taskSize", String.valueOf(taskSizeSpinner.getValue()))
                .build()
                .toString();

        HttpClientUtil.runAsyncGet(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("Could not response well");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try (ResponseBody responseBody = response.body()){
                    if (response.code() == 200) {
                        candidatesTableController.startListRefresher(thereIsWinner());
                        inContest.set(true);
                    }
                    else {
                        System.out.println("Could not response well, url:" + finalUrl);
                        System.out.println(response.code() + "   "  + response.body().string());
                    }
                }
            }
        });
    }

    public void disableBinding(){
        taskSizeSpinner.disableProperty().bind(inContest);
        readyButton.disableProperty().bind(inContest);
    }

    public void inFinishedContest(){

        candidatesTableController.cancelRefresher();
        inContest.setValue(false);
    }

    public void updateEncryptedMessage(String encryptedString){

        encryptedMessageLabel.setText(encryptedString);
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

    private void initialTaskSpinner(double taskSize){

        taskSizeSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1,Integer.MAX_VALUE));
        taskSizeSpinner.valueProperty().addListener((
                observable, oldValue, newValue) -> tasksAmountLabel.setText(String.valueOf((int)Math.ceil(taskSize / (double)newValue))));
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

    Consumer<String> thereIsWinner(){
        return msg -> {
            finishContest();
            parentController.popUpMessage(msg);
        };
    }

    private void finishContest() {

        // should perform all the ui end contest
    }
}
