package components.subControllers;

import com.sun.istack.internal.NotNull;
import components.CandidatesTableController;
import components.PlayerDetailsComponent;
import components.body.details.MachineConfigurationController;
import components.body.main.EncryptController;
import components.body.main.EngineDtoReturnableParentController;
import components.body.main.encryptParentController;
import components.main.UBoatMainAppController;
import contestDtos.ActivePlayerDTO;
import contestDtos.CandidateDataDTO;
import http.HttpClientUtil;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import machineDtos.EngineDTO;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import util.CandidatesRefresher;
import util.CandidatesUpdate;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static constants.Constants.REFRESH_RATE;
import static util.Constants.REQUEST_PATH_SET_READY;

public class UBoatRoomContestController implements encryptParentController, EngineDtoReturnableParentController {

    private UBoatMainAppController parentController;
    private Timer timer;
    private TimerTask listRefresher;
    private BooleanProperty autoUpdate;
    @FXML
    private MachineConfigurationController machineConfigurationController;
    @FXML
    private BorderPane machineConfigurationComponent;
    @FXML
    private EncryptController encryptComponentController;
    @FXML
    private GridPane encryptComponent;
    @FXML
    private FlowPane activeTeamsDetailsFlowPane;
    @FXML
    private Button readyButton;
    @FXML
    private Button logoutButton;
    @FXML
    private AnchorPane candidatesTableComponent;
    @FXML
    private CandidatesTableController candidatesTableController;

    @FXML
    void logoutButtonListener(ActionEvent event) {
        //delete from session

        //delete contest from all allies dashboard

        //return to login page
        parentController.close();
    }

    @FXML
    void readyButtonListener(ActionEvent event) {

        candidatesTableController.startListRefresher();

        String encryptedMessage = String.valueOf(encryptComponentController.getEncryptedMessage());

        String finalUrl = HttpUrl
                .parse(REQUEST_PATH_SET_READY)
                .newBuilder()
                .addQueryParameter("encryptedMessage", encryptedMessage)
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("Could not response well");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (!response.isSuccessful()) {
                    System.out.println("Could not response well, url:" + finalUrl);
                }
                //add the competitors
                System.out.println("encrypted message was updated and now the server is waiting for the teams to set ready!");
                encryptComponent.setDisable(true);//need to change after contest is finished (end of contest servlet)
            }
        });

    }

    @FXML
    public void initialize(){

        if(machineConfigurationController != null){
            machineConfigurationController.setParentController(this);
            machineConfigurationComponent.disableProperty().bind(machineConfigurationController.getIsCodeConfigurationSetProperty().not());
            //encryptScreenMachineConfigurationComponentController.bind(machineConfigurationComponentController);
            encryptComponent.disableProperty().bind(machineConfigurationController.getIsCodeConfigurationSetProperty().not());
            //encryptScreenMachineConfigurationComponent.disableProperty().bind(machineConfigurationComponent.disableProperty());
        }
        if(encryptComponentController != null) {
            encryptComponentController.setParentController(this);
            //encryptComponentController.activateEncryptEventHandler.addListener(parentController.getEncryptMessageEventListener());
            machineConfigurationController.getIsCodeConfigurationSetProperty().addListener(observable -> encryptComponentController.createKeyboards(parentController.getEngineDetails().getEngineComponentsInfo().getABC()));
        }
    }

    public ObjectProperty<EventHandler<ActionEvent>> encryptResetButtonActionProperty(){

        return encryptComponentController.getResetButtonActionProperty();
    }

    public void setUBoatRoomController(UBoatMainAppController uBoatRoomController) {
        this.parentController = uBoatRoomController;
    }

    public void addNewTeamDetails(ActivePlayerDTO newTeam){
        activeTeamsDetailsFlowPane.getChildren().add(new PlayerDetailsComponent(newTeam, "allies"));
    }

    public void setIsCodeConfigurationSet(Boolean codeSet){
        machineConfigurationController.getIsCodeConfigurationSetProperty().set(codeSet);
    }

    public void clearDetails(){
        activeTeamsDetailsFlowPane.getChildren().clear();
        candidatesTableController.clear();
        machineConfigurationController.clearComponent();
        encryptComponentController.clearButtonActionListener(new ActionEvent());
        encryptComponentController.removeOldAbcFromKeyboards();
    }

    public MachineConfigurationController getMachineConfigurationController(){
        return machineConfigurationController;
    }

    public EncryptController getEncryptComponentController(){
        return encryptComponentController;
    }

    @Override
    public EngineDTO getEngineDetails() {
        return null;
    }

    //not in use here
    @Override
    public StringProperty getMachineEncryptedMessageProperty() {
        return null;
    }

}
