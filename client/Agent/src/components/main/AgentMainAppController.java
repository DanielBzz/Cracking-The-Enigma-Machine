package components.main;

import com.sun.istack.internal.NotNull;
import components.subComponents.AgentProgressAndStatusController;
import components.subComponents.ContestAndTeamDataController;
import contestDtos.AgentInfoDTO;
import contestDtos.AgentProgressDTO;
import contestDtos.CandidateDataDTO;
import contestDtos.ContestDetailsDTO;
import http.HttpClientUtil;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import logic.AgentLogic;
import mainapp.AppMainController;
import mainapp.ClientMainController;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import java.io.IOException;
import java.util.List;

import static constants.Constants.AGENT_TYPE;
import static util.Constants.REQUEST_PATH_GET_AGENT_INFO;

public class AgentMainAppController implements AppMainController {
    private ClientMainController parentController;
    private AgentLogic agentLogic;
    @FXML private ScrollPane contestAndTeamDataComponent;
    @FXML private ContestAndTeamDataController contestAndTeamDataComponentController;
    @FXML private ScrollPane agentProgressAndStatusComponent;
    @FXML private AgentProgressAndStatusController agentProgressAndStatusComponentController;
    @FXML private TableView<CandidateDataDTO> candidatesTable;

    @FXML
    public void initialize(){

        getBasicInfo(this);
        if(agentProgressAndStatusComponentController!= null){
            agentProgressAndStatusComponentController.setAgentMainAppController(this);
        }
        candidatesTable.getColumns().get(0).setCellValueFactory(new PropertyValueFactory("decryptedMessage"));
        candidatesTable.getColumns().get(1).setCellValueFactory(new PropertyValueFactory("foundersName"));
        candidatesTable.getColumns().get(2).setCellValueFactory(new PropertyValueFactory("configuration"));
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
        candidatesTable.getItems().clear();
    }

    @Override
    public void loadClientMainPage() {

    }

    public void addContestDetailsToScreen(ContestDetailsDTO contestData){
        contestAndTeamDataComponentController.updateContestData(contestData);
    }
    public synchronized void updateCandidates(List<CandidateDataDTO> newCandidates) {
        newCandidates.forEach(candidate->candidatesTable.getItems().add(candidate));
    }

    public void setActive(){
        //agentProgressAndStatusComponentController.startListRefresher(Constants.REQUEST_PATH_GET_TASKS_DATA);
        //contestAndTeamDataComponentController.startListRefresher(Constants.REQUEST_PATH_GET_CONTESTS);
    }

    public void setPassive(){
        //agentProgressAndStatusComponentController.stopListRefresher();
    }
    public void getBasicInfo(AgentMainAppController thisController){
        HttpClientUtil.runAsyncGet(REQUEST_PATH_GET_AGENT_INFO, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("Could not response well");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() == 200) {
                    AgentInfoDTO basicAgentInfo = constants.Constants.GSON_INSTANCE.fromJson(response.body().string(), AgentInfoDTO.class);
                    agentLogic = new AgentLogic(thisController, basicAgentInfo.getAgentName(), basicAgentInfo.getAmountOfTasksInSingleTake(), basicAgentInfo.getAmountOfThreads());

                    Platform.runLater(()->{
                        contestAndTeamDataComponentController.setAgentMainAppController(thisController);
                        contestAndTeamDataComponentController.setTeamNameLabel(basicAgentInfo.getTeamName());
                        contestAndTeamDataComponentController.initial();
                    });

                    response.body().close();
                    System.out.println("Allies was added successfully!");
                }
                else {
                    System.out.println("Could not response well, url:" + REQUEST_PATH_GET_AGENT_INFO);
                }
            }
        });
    }
}
