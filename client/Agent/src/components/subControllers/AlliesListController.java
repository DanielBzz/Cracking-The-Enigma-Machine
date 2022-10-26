package components.subControllers;

import constants.Constants;
import contestDtos.ActivePlayerDTO;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import util.RefresherController;

public class AlliesListController extends RefresherController {

    private EnteringAgentDetailsController parentController;
    @FXML
    private TableView<ActivePlayerDTO> alliesTable;

    public void addTeam(ActivePlayerDTO newTeam){
        alliesTable.getItems().add(newTeam);
    }

    public void cleanTable(){
        alliesTable.getItems().clear();
    }

    public void setAlliesDashboardController(EnteringAgentDetailsController enteringAgentDetailsController) {
        this.parentController = enteringAgentDetailsController;
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
