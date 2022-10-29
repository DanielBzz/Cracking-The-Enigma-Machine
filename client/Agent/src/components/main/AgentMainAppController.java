package components.main;

import com.sun.istack.internal.NotNull;
import components.CandidatesTableController;
import components.subComponents.AgentProgressAndStatusController;
import components.subComponents.ContestAndTeamDataController;
import contestDtos.AgentInfoDTO;
import contestDtos.AgentProgressDTO;
import http.HttpClientUtil;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import logic.AgentLogic;
import mainapp.AppMainController;
import mainapp.ClientMainController;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import java.io.IOException;

import static util.Constants.REQUEST_PATH_GET_AGENT_INFO;

public class AgentMainAppController implements AppMainController {
    private ClientMainController parentController;
    private AgentLogic agentLogic;

    @FXML
    private ScrollPane contestAndTeamDataComponent;
    @FXML
    private ContestAndTeamDataController contestAndTeamDataComponentController;

    @FXML
    private ScrollPane agentProgressAndStatusComponent;
    @FXML
    private AgentProgressAndStatusController agentProgressAndStatusComponentController;

    @FXML
    private ScrollPane agentsCandidatesComponent;
    @FXML
    private CandidatesTableController agentsCandidatesComponentController;

    @FXML
    public void initialize(){
        getBasicInfo(this);
        if(agentProgressAndStatusComponentController!= null){
            agentProgressAndStatusComponentController.setAgentMainAppController(this);
        }
        if(agentsCandidatesComponentController!= null){
            agentsCandidatesComponentController.setWhoFoundTheAnswerLabel("agent");
        }
    }

    public void updateTasksData(AgentProgressDTO data){
        agentProgressAndStatusComponentController.updateData(data);
    }

    @Override
    public void setClientMainController(ClientMainController clientMainController) {
        this.parentController = clientMainController;
    }

    @Override
    public void close() {
        agentLogic.logOut();
        clearComponent();
        parentController.switchToLogin();
    }

    @Override
    public void clearComponent() {
        contestAndTeamDataComponentController.clearTable();
        agentProgressAndStatusComponentController.clearDetails();
        agentsCandidatesComponentController.clear();
    }

    @Override
    public void loadClientMainPage() {

    }

    public void setActive(){
        //agentProgressAndStatusComponentController.startListRefresher(Constants.REQUEST_PATH_GET_TASKS_DATA);
        //contestAndTeamDataComponentController.startListRefresher(Constants.REQUEST_PATH_GET_CONTESTS);
        agentsCandidatesComponentController.startListRefresher();
    }

    public void setPassive(){
        //agentProgressAndStatusComponentController.stopListRefresher();
        agentsCandidatesComponentController.cancelRefresher();
    }
    public void getBasicInfo(AgentMainAppController thisController){
        HttpClientUtil.runAsync(REQUEST_PATH_GET_AGENT_INFO, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("Could not response well");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() == 200) {
                    if(response.body() != null){
                        AgentInfoDTO basicAgentInfo = constants.Constants.GSON_INSTANCE.fromJson(response.body().toString(), AgentInfoDTO.class);
                        agentLogic = new AgentLogic(thisController, basicAgentInfo.getAgentName(), basicAgentInfo.getAmountOfTasksInSingleTake(), basicAgentInfo.getAmountOfThreads());
                        if(contestAndTeamDataComponentController!= null) {
                            contestAndTeamDataComponentController.setAgentMainAppController(thisController);
                            contestAndTeamDataComponentController.setTeamNameLabel(basicAgentInfo.getTeamName());
                        }
                        response.body().close();
                    }

                    System.out.println("Allies was added successfully!");
                }
                else {
                    System.out.println("Could not response well, url:" + REQUEST_PATH_GET_AGENT_INFO);
                }
            }
        });
    }
}
