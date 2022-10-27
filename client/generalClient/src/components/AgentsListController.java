package components;

import constants.Constants;
import contestDtos.ActiveAgentDTO;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import util.RefresherController;

public class AgentsListController extends RefresherController {

    @FXML private TableView<ActiveAgentDTO> alliesTable;

    public void addTeam(ActiveAgentDTO newAgent){
        alliesTable.getItems().add(newAgent);
    }

    public void cleanTable(){
        alliesTable.getItems().clear();
    }


    @Override
    public void updateList(String jsonUserList) {
        ActiveAgentDTO[] usersList = Constants.GSON_INSTANCE.fromJson(jsonUserList,ActiveAgentDTO[].class);

        cleanTable();
        for(ActiveAgentDTO agent : usersList){
            addTeam(agent);
        }
    }
}
