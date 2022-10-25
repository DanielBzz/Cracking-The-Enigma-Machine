package components.subControllers;

import contestDtos.ActivePlayerDTO;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;

public class AlliesListController {

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
}
