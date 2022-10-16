package components;

import contestDtos.CandidateDataDTO;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class CandidatesTableController {
    @FXML
    private TableView<CandidateDataDTO> candidatesTable;

    @FXML
    private TableColumn<CandidateDataDTO, String> whoFoundTheAnswerLabel;

    public void setWhoFoundTheAnswerLabel(String foundAnswersType){
        whoFoundTheAnswerLabel.setText(foundAnswersType);
    }

    public void addNewCandidate(CandidateDataDTO newCandidate){
        candidatesTable.getItems().add(newCandidate);
    }
    public void clear(){
        candidatesTable.getItems().clear();
    }
}
