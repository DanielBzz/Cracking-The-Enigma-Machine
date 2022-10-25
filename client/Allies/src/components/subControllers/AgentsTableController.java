package components.subControllers;

import components.main.AlliesMainAppController;
import contestDtos.ActivePlayerDTO;
import contestDtos.ContestDetailsDTO;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;

public class AgentsTableController {
    private AlliesDashboardController parentController;
    @FXML
    private TableView<ActivePlayerDTO> agentsTable;

    public void addAgent(ActivePlayerDTO newAgent){
        agentsTable.getItems().add(newAgent);
    }

    public void cleanTable(){
        agentsTable.getItems().clear();
    }

    public void setAlliesDashboardController(AlliesDashboardController alliesDashboardController) {
        this.parentController = alliesDashboardController;
    }

}
