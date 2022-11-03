package components.subComponents;

import com.sun.istack.internal.NotNull;
import components.ActivePlayerListController;
import components.DynamicComponent;
import http.HttpClientUtil;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.util.StringConverter;
import mainapp.AppMainController;
import mainapp.ClientMainController;
import okhttp3.*;
import util.Constants;
import util.Presenter;
import util.tableHolderInterfaces.TeamTableHolder;

import java.io.IOException;

import static constants.Constants.REQUEST_PATH_USERS_UPDATE;
import static util.Constants.REQUEST_PATH_ADD_AGENT_TO_TEAM;


public class EnteringAgentDetailsController implements Presenter, AppMainController, TeamTableHolder {

    private ClientMainController mainAppController;
    @FXML private AnchorPane alliesListPlace;
    private ActivePlayerListController alliesListComponentController;
    @FXML private Slider amountOfAgentsSlider;
    @FXML private Spinner<Integer> amountOfTasksSpinner;

    @FXML
    public void initialize() {
        try {
            FXMLLoader load = new FXMLLoader();
            load.setLocation(ActivePlayerListController.class.getResource("activePlayerList.fxml"));
            Node newComponent = load.load();
            alliesListPlace.getChildren().add(newComponent);
            DynamicComponent.fitToPane(newComponent);

            alliesListComponentController = load.getController();
            alliesListComponentController.setTableHolder(this);
            alliesListComponentController.setChooseable(true);
            alliesListComponentController.startListRefresher(REQUEST_PATH_USERS_UPDATE);

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

        initialTaskSpinner();
    }

    @FXML
    void readyButtonListener(ActionEvent event) {

        String teamManager = alliesListComponentController.getSelectedUserName();
        String amountOfThreads = String.valueOf((int)amountOfAgentsSlider.getValue());
        String amountOfTasksInASingleTake = String.valueOf(amountOfTasksSpinner.getValue());

        String finalUrl = HttpUrl
                .parse(REQUEST_PATH_ADD_AGENT_TO_TEAM)
                .newBuilder()
                .addQueryParameter("teamManager", teamManager)
                .addQueryParameter("amountOfThreads", amountOfThreads)
                .addQueryParameter("amountOfTasksInASingleTake", amountOfTasksInASingleTake)
                .build()
                .toString();

        System.out.println("after pressing ready button in second screen of agent, before request been sent");
        HttpClientUtil.runAsyncGet(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("Could not response well");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                try(ResponseBody responseBody = response.body()){
                    if(response.code() == 200){
                        System.out.println("on response to add agent to team, in second screen of agent");
                        alliesListComponentController.stopListRefresher();
                        Platform.runLater(() -> {
                            mainAppController.loadMainAppForm(getClass().getResource(Constants.AGENT_MAIN_APP_FXML_RESOURCE_LOCATION),Constants.AGENT_CLIENT);
                            mainAppController.switchToMainApp();
                        });
                    }

                    if (!response.isSuccessful()) {
                        System.out.println("Could not response well, url:" + finalUrl);
                        System.out.printf(response.code() + "   " + responseBody.string());
                    }
                }
            }
        });
    }

    @Override
    public void setClientMainController(ClientMainController clientMainController) {
        this.mainAppController = clientMainController;
    }

    private void initialTaskSpinner(){

        amountOfTasksSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1,Integer.MAX_VALUE));
        amountOfTasksSpinner.getValueFactory().setConverter(new StringConverter<Integer>() {
            @Override
            public String toString(Integer object) {return object.toString();}
            @Override
            public Integer fromString(String string) {
                try {
                    return Integer.parseInt(string);
                }catch (NumberFormatException e){
                    return 2;
                }
            }
        });
        amountOfTasksSpinner.getValueFactory().setValue(1);
    }

    @Override
    public void close() {

    }

    @Override
    public void clearComponent() {

    }
}
