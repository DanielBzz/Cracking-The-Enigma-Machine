package components;

import constants.Constants;
import contestDtos.ContestDetailsDTO;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import util.RefresherController;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ContestDetailsTableController extends RefresherController {
    @FXML private TableView<ContestDetailsDTO> detailsTable;

    @FXML
    public void initialize(){
        detailsTable.getColumns().get(0).setCellValueFactory(new PropertyValueFactory("battleFieldName"));
        detailsTable.getColumns().get(1).setCellValueFactory(new PropertyValueFactory("contestManagerName"));
        detailsTable.getColumns().get(2).setCellValueFactory(new PropertyValueFactory("status"));
        detailsTable.getColumns().get(3).setCellValueFactory(new PropertyValueFactory("level"));
        detailsTable.getColumns().get(4).setCellValueFactory(new PropertyValueFactory("teams"));
    }

    @Override
    public void updateList(String jsonUserList){
        List<ContestDetailsDTO> contests = Arrays.asList(Constants.GSON_INSTANCE.fromJson(jsonUserList,ContestDetailsDTO[].class));
        String nameOfChosen = getChosenContestUserName();
        Optional<ContestDetailsDTO> chosenContest = nameOfChosen != null ?
                contests.stream().filter(contest -> contest.getContestManagerName().equals(nameOfChosen)).findFirst()
                : Optional.empty();

        cleanTable();
        contests.forEach(this::addContest);

        if(nameOfChosen!= null && chosenContest.isPresent()){
            detailsTable.getSelectionModel().select(chosenContest.get());
        }
    }

    public void addContest(ContestDetailsDTO newContest){

        detailsTable.getItems().add(newContest);
    }

    public void cleanTable(){
        if(detailsTable !=null && detailsTable.getItems().size() != 0) {
            detailsTable.getItems().clear();
        }
    }

    public String getChosenContestUserName(){

        return detailsTable.getSelectionModel().getSelectedItem() != null ?
                detailsTable.getSelectionModel().getSelectedItem().getContestManagerName() :
                null;
    }
}
