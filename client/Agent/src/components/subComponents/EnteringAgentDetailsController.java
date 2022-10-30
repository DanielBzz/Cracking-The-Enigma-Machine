package components.subComponents;

import com.sun.istack.internal.NotNull;
import components.AlliesListController;
import components.DynamicComponent;
import http.HttpClientUtil;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import mainapp.AppMainController;
import mainapp.ClientMainController;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import util.Constants;
import util.Presenter;

import java.io.IOException;

import static constants.Constants.REQUEST_PATH_USERS_UPDATE;
import static util.Constants.REQUEST_PATH_ADD_AGENT_TO_TEAM;


public class EnteringAgentDetailsController implements Presenter, AppMainController {
    ClientMainController mainAppController;

    @FXML private AnchorPane alliesListPlace;
    private AlliesListController alliesListComponentController;
    @FXML private Slider amountOfAgentsSlider;
    @FXML private TextField amountOfTasksField;

    //need to make it relevant to agents

    @FXML
    public void initialize() {
        try {
            FXMLLoader load = new FXMLLoader();
            load.setLocation(AlliesListController.class.getResource("allies-list.fxml"));
            Node newComponent = load.load();
            alliesListPlace.getChildren().add(newComponent);
            DynamicComponent.fitToPane(newComponent);

            alliesListComponentController = load.getController();
            alliesListComponentController.startListRefresher(REQUEST_PATH_USERS_UPDATE);

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
    @FXML
    void readyButtonListener(ActionEvent event) {

        String teamManager = alliesListComponentController.getSelectedAlliesName();
        String amountOfThreads = String.valueOf(amountOfAgentsSlider.getValue());
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
                if (!response.isSuccessful()) {
                    System.out.println("Could not response well, url:" + finalUrl);
                }

                System.out.println("Agent was added successfully!");
                alliesListComponentController.stopListRefresher();
                //need to check if it works
                Platform.runLater(() -> {
                    mainAppController.loadMainAppForm(getClass().getResource(Constants.AGENT_MAIN_APP_FXML_RESOURCE_LOCATION),Constants.AGENT_CLIENT);
                    mainAppController.switchToMainApp();
                });

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

    @Override
    public void loadClientMainPage() {

    }
}
