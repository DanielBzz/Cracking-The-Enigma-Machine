package components;

import contestDtos.CandidateDataDTO;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import util.CandidatesRefresher;
import util.WinnerChecker;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

import static constants.Constants.REFRESH_RATE;

public class CandidatesTableController {

    private WinnerChecker<CandidateDataDTO> winnerChecker;
    @FXML private TableView<CandidateDataDTO> candidatesTable;
    @FXML private TableColumn<CandidateDataDTO, String> whoFoundTheAnswerLabel;
    private TimerTask candidatesRefresher;
    private Timer timer;

    //can be checked after starting a contest
    @FXML
    public void initialize(){
        candidatesTable.getColumns().get(0).setCellValueFactory(new PropertyValueFactory("decryptedMessage"));
        candidatesTable.getColumns().get(1).setCellValueFactory(new PropertyValueFactory("foundersName"));
        candidatesTable.getColumns().get(2).setCellValueFactory(new PropertyValueFactory("configuration"));
    }
    public void startListRefresher(Consumer<String> finishContestConsumer) {
        candidatesRefresher = new CandidatesRefresher(this::updateCandidates,finishContestConsumer,0);
        timer = new Timer();
        timer.schedule(candidatesRefresher, REFRESH_RATE/4, REFRESH_RATE/4);
    }

    public void cancelRefresher(){
        timer.cancel();
    }

    public void updateCandidates(List<CandidateDataDTO> newCandidates) {

        Platform.runLater(() -> {
            newCandidates.forEach(this::addNewCandidate);
        });
    }

    public void addNewCandidate(CandidateDataDTO newCandidate){

        candidatesTable.getItems().add(newCandidate);
        if(winnerChecker!= null){
            winnerChecker.checkIfWinner(newCandidate);
        }
    }

    public void clear(){
        candidatesTable.getItems().clear();
        if(timer!=null){
            timer.cancel();
        }
    }

    public void setWhoFoundTheAnswerLabel(String foundAnswersType){
        whoFoundTheAnswerLabel.setText(foundAnswersType);
    }

    public void setWinnerChecker(WinnerChecker<CandidateDataDTO> winnerChecker) {
        this.winnerChecker = winnerChecker;
    }
}