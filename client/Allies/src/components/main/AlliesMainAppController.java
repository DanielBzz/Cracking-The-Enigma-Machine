package components.main;

import components.subControllers.AlliesContestController;
import components.subControllers.AlliesDashboardController;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import mainapp.AppMainController;
import mainapp.ClientMainController;

public class AlliesMainAppController implements AppMainController {
    private ClientMainController parentController;

    @FXML
    private GridPane dashboardComponent;
    @FXML
    private AlliesDashboardController dashboardController;
    @FXML
    private GridPane contestComponent;
    @FXML
    private AlliesContestController contestController;

    @Override
    public void setClientMainController(ClientMainController clientMainController) {
        this.parentController = clientMainController;
    }

    @Override
    public void close() {
        clearComponent();
        parentController.switchToLogin();
    }

    @Override
    public void clearComponent() {

    }

    @Override
    public void loadClientMainPage() {

    }
}
