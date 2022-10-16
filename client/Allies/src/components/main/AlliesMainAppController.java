package components.main;

import components.subControllers.AlliesContestController;
import components.subControllers.AlliesDashboardController;
import components.subControllers.ContestDetailsController;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import mainapp.AppMainController;
import mainapp.ClientMainController;
import okhttp3.Response;

public class AlliesMainAppController implements AppMainController {
    private ClientMainController parentController;
    //maybe change the list<agents> to int only to know how many agents do i have
    private List<Agent> agents;
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

    public void changeContest(ContestDetailsController newContest){
        contestController.changeContest(newContest);
    }
    public void addAgent(Agent newAgent){
        agents.add(newAgent);
    }

    public int getAmountOfAgents(){
        return agents.size();
    }

    public int getTaskSize(){
        return AlliesDashboardController.getTaskSize();
    }

    public void createCompetitorsFromResponse(Response response){
        contestController.createCompetitorsFromResponse(response);
    }
}
