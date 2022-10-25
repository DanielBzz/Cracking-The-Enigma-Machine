package components;

import contestDtos.ContestDetailsDTO;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import util.ContestPresenter;

public class ContestDetailsTableController implements ContestPresenter {
    private ContestPresenter parentController;
    @FXML
    private TableView<ContestDetailsDTO> detailsTable;

    public void addContest(ContestDetailsDTO newContest){
        detailsTable.getItems().add(newContest);
    }

    public void cleanTable(){
        detailsTable.getItems().clear();
    }

    public ContestDetailsDTO getSelectedContest(){
        return detailsTable.getSelectionModel().getSelectedItem();
    }

    public void setParentController(ContestPresenter parentController) {
        this.parentController = parentController;
    }

    //need to use the refresher (all the information all the time)
}
