package components;

import constants.Constants;
import contestDtos.ContestDetailsDTO;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import util.ContestPresenter;
import util.RefresherController;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ContestDetailsTableController extends RefresherController {
    private ContestPresenter parentController;
    @FXML private TableView<ContestDetailsDTO> detailsTable;

    @Override
    public void updateList(String jsonUserList){
        List<ContestDetailsDTO> contests = Arrays.asList(Constants.GSON_INSTANCE.fromJson(jsonUserList,ContestDetailsDTO[].class));
        String nameOfChosen = getSelectedContest() != null ? getSelectedContest().getContestManagerName() : null;
        Optional<ContestDetailsDTO> chosenContest = nameOfChosen != null ?
                contests.stream().filter(contest -> contest.getContestManagerName().equals(nameOfChosen)).findFirst()
                : Optional.empty();

        detailsTable.setItems(FXCollections.observableList(contests));
        if(nameOfChosen!= null && chosenContest.isPresent()){
            detailsTable.getSelectionModel().select(chosenContest.get());
        }
    }

    public void setParentController(ContestPresenter parentController) {

        this.parentController = parentController;
    }

    public void addContest(ContestDetailsDTO newContest){

        detailsTable.getItems().add(newContest);
    }

    public void cleanTable(){

        detailsTable.getItems().clear();
    }

    public ContestDetailsDTO getSelectedContest(){

        return detailsTable.getSelectionModel().getSelectedItem();
    }
}
