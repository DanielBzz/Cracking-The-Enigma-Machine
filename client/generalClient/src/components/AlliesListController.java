package components;

import constants.Constants;
import contestDtos.ActivePlayerDTO;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import util.RefresherController;

public class AlliesListController extends RefresherController {

    @FXML private TableView<ActivePlayerDTO> alliesTable;

    public void addTeam(ActivePlayerDTO newTeam){
        alliesTable.getItems().add(newTeam);
    }

    public void cleanTable(){
        alliesTable.getItems().clear();
    }


    public String getSelectedAlliesName(){

        return alliesTable.getSelectionModel().getSelectedItem() != null ?
                alliesTable.getSelectionModel().getSelectedItem().getName() :
                null;
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
