package components;

import constants.Constants;
import contestDtos.ActiveAgentDTO;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import util.RefresherController;

public class AgentsListController extends RefresherController {

    @FXML private TableView<ActiveAgentDTO> agentsTable;

    public void addTeam(ActiveAgentDTO newAgent){
        agentsTable.getItems().add(newAgent);
    }

    public void cleanTable(){
        if(agentsTable.getItems()!= null) {
            System.out.println("agentsTable.getItems() = " + agentsTable.getItems());
            agentsTable.getItems().clear();
        }else{
            System.out.println("*****agentTable.getItems() = null*****");
        }
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
