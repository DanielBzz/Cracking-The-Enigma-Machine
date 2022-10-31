package components.main;

import components.subComponents.AlliesContestController;
import components.subComponents.AlliesDashboardController;
import contestDtos.TeamDetailsContestDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.GridPane;
import mainapp.AppMainController;
import mainapp.ClientMainController;

import static constants.Constants.AGENT_TYPE;
import static constants.Constants.ALLIES_TYPE;

public class AlliesMainAppController implements AppMainController {
    private ClientMainController parentController;
    @FXML private GridPane dashboardComponent;
    @FXML private AlliesDashboardController dashboardComponentController;
    @FXML private GridPane contestComponent;
    @FXML private AlliesContestController contestComponentController;
    @FXML private Tab contestTab;
    @FXML private Tab dashboardTab;
    @FXML private TabPane doubleTabPane;

    @FXML
    public void initialize(){

        if(dashboardComponentController!= null) {
            dashboardComponentController.setAlliesMainAppController(this);
            dashboardComponentController.initial();
        }
        if(contestComponentController!= null){
            contestComponentController.setAlliesMainAppController(this);
            contestComponentController.initial();
            contestTab.disableProperty().bind(dashboardTab.disableProperty().not());
        }

    }

    //------------------------- AppMainController interface -----------------------------
    @Override
    public void setClientMainController(ClientMainController clientMainController) {
        this.parentController = clientMainController;
        parentController.getUserNameProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println(newValue);
            if (newValue!= null && !newValue.equals("")) {
                dashboardComponentController.setActive();
            }
        });
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
    //-------------------------------------------------------------------------------------

    public void updateNewContest(TeamDetailsContestDTO responseDetails) {       // get all the details and should update contest component
        // need to enable contest tab and maybe disable first tab
        contestComponentController.setBasicThingsForContest(responseDetails);

    }

    public void ContestInactive() {
        contestComponentController.setInactive();
        contestComponentController.setActive();
        dashboardTab.disableProperty().set(false);
    }

    public void dashboardInactive(){
        dashboardComponentController.setInactive();
        contestComponentController.setActive();
        dashboardTab.disableProperty().set(true);
    }

    public void switchPanes(){
        if(doubleTabPane.getSelectionModel().getSelectedIndex() == 1){
            doubleTabPane.getSelectionModel().select(0);
        }else{
            doubleTabPane.getSelectionModel().select(1);
        }
    }
}
