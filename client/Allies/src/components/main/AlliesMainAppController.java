package components.main;

import components.subComponents.AlliesContestController;
import components.subComponents.AlliesDashboardController;
import contestDtos.TeamDetailsContestDTO;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import mainapp.AppMainController;
import mainapp.ClientMainController;

public class AlliesMainAppController implements AppMainController {
    private ClientMainController parentController;
    @FXML private GridPane dashboardComponent;
    @FXML private AlliesDashboardController dashboardComponentController;
    @FXML private GridPane contestComponent;
    @FXML private AlliesContestController contestComponentController;

    @FXML
    public void initialize(){

        if(dashboardComponentController!= null) {
            dashboardComponentController.setAlliesMainAppController(this);
            parentController.getUserNameProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.equals("")) {
                    dashboardComponentController.setActive();
                }
            });
        }
        if(contestComponentController!= null){
            contestComponentController.setAlliesMainAppController(this);
        }

    }

    //------------------------- AppMainController interface -----------------------------
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
        dashboardComponentController.clearComponent();
        contestComponentController.clearComponent();
    }

    @Override
    public void loadClientMainPage() {

    }
    //-------------------------------------------------------------------------------------

    public void updateNewContest(TeamDetailsContestDTO responseDetails) {       // get all the details and should update contest component

        // need to enable contest tab and maybe disable first tab
        contestComponentController.activateContestScreen(responseDetails);

    }
}
