package components;

import constants.Constants;
import contestDtos.ActiveAgentDTO;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import util.RefresherController;

public class AgentsListController extends RefresherController {

    @FXML private TableView<ActiveAgentDTO> agentsTable;


    //can be checked after fixing the entering of agents to the contest
    @FXML
    public void initialize(){
        agentsTable.getColumns().get(0).setCellValueFactory(new PropertyValueFactory("name"));
        agentsTable.getColumns().get(1).setCellValueFactory(new PropertyValueFactory("amount"));
        agentsTable.getColumns().get(2).setCellValueFactory(new PropertyValueFactory("size"));
        agentsTable.getColumns().get(3).setCellValueFactory(new PropertyValueFactory("amountOfCandidates"));
    }

    public void addTeam(ActiveAgentDTO newAgent){
        agentsTable.getItems().add(newAgent);
    }

    public void cleanTable(){
        //delete print
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
