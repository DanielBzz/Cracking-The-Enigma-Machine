package components.main;

import components.subControllers.AlliesContestController;
import components.subControllers.AlliesDashboardController;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import mainapp.AppMainController;
import mainapp.ClientMainController;

public class AlliesMainAppController implements AppMainController {
    private ClientMainController parentController;
    //maybe change the list<agents> to int only to know how many agents do i have
    //private List<Agent> agents;
    @FXML private GridPane dashboardComponent;
    @FXML private AlliesDashboardController dashboardComponentController;
    @FXML private GridPane contestComponent;
    @FXML private AlliesContestController contestComponentController;

    @FXML
    public void initialize(){

        if(dashboardComponentController!= null){
            dashboardComponentController.setAlliesMainAppController(this);
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

    }

    @Override
    public void loadClientMainPage() {

    }
    //-------------------------------------------------------------------------------------

//    public void changeContest(ContestDetailsController newContest){
//        contestComponentController.changeContest(newContest);
//    }
//    public void addAgent(Agent newAgent){
//        agents.add(newAgent);
//    }
//
//    public int getAmountOfAgents(){
//        return agents.size();
//    }

    public int getTaskSize(){
        return dashboardComponentController.getTaskSize();
    }

    public void createCompetitorsFromResponse(Response response){
        contestComponentController.createCompetitorsFromResponse(response);
    }
}
