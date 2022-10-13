package login;

import com.sun.istack.internal.NotNull;
import constants.Constants;
import http.HttpClientUtil;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;

import java.io.IOException;

public class LoginController {

    @FXML private TextField userNameTextField;
    @FXML private Label errorMessageLabel;
    private final StringProperty errorMessageProperty = new SimpleStringProperty();
    private Loggable parentController;


    public void setUBoatAppMainController(Loggable parentController) {

        this.parentController = parentController;
    }

    @FXML
    public void initialize() {
        errorMessageLabel.textProperty().bind(errorMessageProperty);
    }

    @FXML
    private void loginButtonClicked(ActionEvent event) {

        String userName = userNameTextField.getText();
        if (userName.isEmpty()) {
            errorMessageProperty.set("User name is empty. You can't login with empty user name");
            System.out.println("User name is empty. You can't login with empty user name");
            return;
        }

        //noinspection ConstantConditions
        System.out.println("--------------after login button was clicked---------");

        String finalUrl = HttpUrl
                        .parse(Constants.REQUEST_PATH_LOGIN)
                        .newBuilder()
                        .addQueryParameter("username", userName)
                        .build()
                        .toString();

        System.out.println(finalUrl);
        HttpClientUtil.runAsync(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("-------on failure-------Something went wrong: " + e.getMessage());
                Platform.runLater(() ->
                        errorMessageProperty.set("Something went wrong: " + e.getMessage())
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    System.out.println("-------on response-------Something went wrong: " + responseBody);
                    Platform.runLater(() ->
                            errorMessageProperty.set("Something went wrong: " + responseBody)
                    );
                } else {
                    System.out.println("success");
                    Platform.runLater(() -> {
                        System.out.println("--------------user name is: " + userName + "---------");
                        parentController.updateUserName(userName);
                        System.out.println("--------------after update user name---------");
                        parentController.switchToMainApp();
                    });
                }
            }
        });
    }

    @FXML private void userNameKeyTyped(KeyEvent event) {

        errorMessageProperty.set("");
    }

    @FXML private void quitButtonClicked(ActionEvent e) {

        Platform.exit();
    }
}
