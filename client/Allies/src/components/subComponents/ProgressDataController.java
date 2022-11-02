package components.subComponents;

import constants.Constants;
import contestDtos.AlliesProgressDTO;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import util.RefresherController;

public class ProgressDataController extends RefresherController {

    private AlliesContestController parentController;
    @FXML private Label totalTasksLabel;
    @FXML private Label producedTasksLabel;
    @FXML private Label finishedTasksLabel;
    @FXML private ProgressBar decryptionProgressBar;
    @FXML private Label decryptionProgressLabel;
    private FloatProperty progress = new SimpleFloatProperty();

    public void initial(){
        progress.setValue(0);
        decryptionProgressBar.progressProperty().bind(progress);
        decryptionProgressLabel.textProperty().bind(Bindings.concat(
                Bindings.format(
                        "%.0f",
                        Bindings.multiply(
                                progress,
                                100)),
                " %"));
    }

    public void setAlliesContestController(AlliesContestController alliesContestController) {
        this.parentController = alliesContestController;
    }

    public void updateTotalTasksLabel(int newValue){
        totalTasksLabel.setText(String.valueOf(newValue));
    }

    public void updateProducedTasksLabel(int newValue){
        producedTasksLabel.setText(String.valueOf(newValue));
    }
    public void updateFinishedTasksLabel(int newValue){
        finishedTasksLabel.setText(String.valueOf(newValue));
    }

    @Override
    public void updateList(String jsonUserList) {
        AlliesProgressDTO progressData = Constants.GSON_INSTANCE.fromJson(jsonUserList, AlliesProgressDTO.class);
        producedTasksLabel.setText(String.valueOf(progressData.getProducedTasks()));
        finishedTasksLabel.setText(String.valueOf(progressData.getFinishedTasks()));
        if(Integer.parseInt(totalTasksLabel.getText()) == 0){
            Platform.runLater(()->progress.setValue(0));

        }
        else{
            System.out.println("********************in progressDataControler***************************");
            System.out.println((progressData.getFinishedTasks() / Double.parseDouble(totalTasksLabel.getText())));
            Platform.runLater(()->progress.set((float) (progressData.getFinishedTasks() / Double.parseDouble(totalTasksLabel.getText()))));
        }
    }

    public void clearComponent(){
        progress.setValue(0);
        totalTasksLabel.setText("");
        producedTasksLabel.setText("");
        finishedTasksLabel.setText("");
    }

}
