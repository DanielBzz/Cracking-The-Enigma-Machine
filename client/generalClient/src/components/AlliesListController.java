package components;

import constants.Constants;
import contestDtos.ActivePlayerDTO;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import util.Presenter;
import util.RefresherController;

public class AlliesListController extends RefresherController {

    private Presenter parentController;
    @FXML
    private TableView<ActivePlayerDTO> alliesTable;

    public void addTeam(ActivePlayerDTO newTeam){
        alliesTable.getItems().add(newTeam);
    }

    public void cleanTable(){
        alliesTable.getItems().clear();
    }

    public void setAlliesDashboardController(Presenter alliesPresenter) {
        this.parentController = alliesPresenter;
    }

    public String getSelectedAlliesName(){
        return alliesTable.getSelectionModel().getSelectedItem().getName();
    }

    @Override
    public void updateList(String jsonUserList) {
        ActivePlayerDTO[] usersList = Constants.GSON_INSTANCE.fromJson(jsonUserList,ActivePlayerDTO[].class);

        cleanTable();
        for(ActivePlayerDTO player : usersList){
            addTeam(player);
        }
    }
}
