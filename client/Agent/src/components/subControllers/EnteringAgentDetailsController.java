package components.subControllers;

import com.sun.istack.internal.NotNull;
import http.HttpClientUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;

import java.io.IOException;


public class EnteringAgentDetailsController {

    @FXML
    private ScrollPane alliesListComponent;
    @FXML
    private AlliesListController alliesListComponentController;

    @FXML
    private Slider amountOfAgentsSlider;

    @FXML
    private TextField amountOfTasksField;

    //need to make it relevant to agents
    @FXML
    void readyButtonListener(ActionEvent event) {

        String contestManagerName = contestTableComponentController.getSelectedContest().getContestManagerName();

        String finalUrl = HttpUrl
                .parse(REQUEST_PATH_JOIN_TO_CONTEST)
                .newBuilder()
                .addQueryParameter("contestManager", contestManagerName)
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
                System.out.println("Allies was added successfully!");
                parentController.createCompetitorsFromResponse(response);
            }
        });

     //   parentController.changeContest(contestTableComponentController.getSelectedContest());
    }
}
