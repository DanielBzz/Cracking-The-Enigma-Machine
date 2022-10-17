package components;

import contestDtos.CandidateDataDTO;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import util.CandidatesRefresher;
import util.CandidatesUpdate;

import java.util.Timer;
import java.util.TimerTask;

import static constants.Constants.REFRESH_RATE;

public class CandidatesTableController implements CandidatesUpdate {
    private Timer timer;
    private TimerTask listRefresher;
    private BooleanProperty autoUpdate;
    @FXML
    private TableView<CandidateDataDTO> candidatesTable;

    @FXML
    private TableColumn<CandidateDataDTO, String> whoFoundTheAnswerLabel;
    private CandidatesRefresher candidatesRefresher;

    public void setWhoFoundTheAnswerLabel(String foundAnswersType){
        whoFoundTheAnswerLabel.setText(foundAnswersType);
    }

    public void addNewCandidate(CandidateDataDTO newCandidate){
        candidatesTable.getItems().add(newCandidate);
    }
    public void clear(){
        candidatesTable.getItems().clear();
    }

    @Override
    public void updateCandidates(CandidateDataDTO candidate) {
        Platform.runLater(() -> {
            //candidates.forEach(candidate->candidatesTableController.addNewCandidate(candidate));
            addNewCandidate(candidate);
        });
    }



    //need constants for "http://localhost:8080/enigmaServer/contestManager"

    public void startListRefresher() {
        candidatesRefresher = new CandidatesRefresher(
                "http://localhost:8080/enigmaServer/contestManager",
                this::updateCandidates,
                autoUpdate);
        timer = new Timer();
        timer.schedule(listRefresher, REFRESH_RATE, REFRESH_RATE);
    }
}
