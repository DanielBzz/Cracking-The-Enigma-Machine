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
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
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
    ClientMainController mainAppController;
    @FXML private AnchorPane alliesListPlace;
    private ActivePlayerListController alliesListComponentController;
    @FXML private Slider amountOfAgentsSlider;
    @FXML private TextField amountOfTasksField;

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
    }

    @FXML
    void readyButtonListener(ActionEvent event) {

        String teamManager = alliesListComponentController.getSelectedAlliesName();
        String amountOfThreads = String.valueOf((int)amountOfAgentsSlider.getValue());
        String amountOfTasksInASingleTake = amountOfTasksField.getText();

        String finalUrl = HttpUrl
                .parse(REQUEST_PATH_ADD_AGENT_TO_TEAM)
                .newBuilder()
                .addQueryParameter("teamManager", teamManager)
                .addQueryParameter("amountOfThreads", amountOfThreads)
                .addQueryParameter("amountOfTasksInASingleTake", amountOfTasksInASingleTake)
                .build()
                .toString();

        HttpClientUtil.runAsyncGet(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("Could not response well");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                try(ResponseBody responseBody = response.body()){
                    if(response.code() == 200){
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

    @Override
    public void close() {

    }

    @Override
    public void clearComponent() {

    }
}
