package components.tables;

import constants.Constants;
import contestDtos.ActivePlayerDTO;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import util.RefresherController;

public class AgentsTableController extends RefresherController {

    @FXML private TableView<ActivePlayerDTO> agentsTable;

    @Override
    public void updateList(String jsonUserList) {

        ActivePlayerDTO[] usersList = Constants.GSON_INSTANCE.fromJson(jsonUserList,ActivePlayerDTO[].class);

        cleanTable();
        for(ActivePlayerDTO player : usersList){
            addAgent(player);
        }
    }

    public void addAgent(ActivePlayerDTO newAgent){

        agentsTable.getItems().add(newAgent);
    }

    public void cleanTable(){

        agentsTable.getItems().clear();
    }
}
