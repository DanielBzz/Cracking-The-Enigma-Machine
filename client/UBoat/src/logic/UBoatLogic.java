package logic;

import components.main.UBoatMainAppController;
import contestDtos.CandidateDataDTO;
import decryptionDtos.DictionaryDTO;
import http.HttpClientUtil;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import logic.events.CodeSetEventListener;
import logic.events.handler.MachineEventHandler;
import machineDtos.EngineDTO;
import machineDtos.EngineWithEncryptDTO;
import machineDtos.MachineInfoDTO;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import util.Constants;

import java.io.File;
import java.io.IOException;


public class UBoatLogic {

    private final UBoatMainAppController appController;
    public MachineEventHandler<CodeSetEventListener> codeSetEventHandler = new MachineEventHandler<>();
    private final StringProperty encryptedMessage = new SimpleStringProperty();


    public UBoatLogic(UBoatMainAppController appController) {

        this.appController = appController;
    }

    public void uploadFileToServer(String filePath) {

        File f = new File(filePath);
        if (!f.exists()) {
            return;
        }
        System.out.println(filePath);
        RequestBody body =
                new MultipartBody.Builder()
                        .addFormDataPart("file", f.getName(), RequestBody.create(f, MediaType.parse("text/plain")))
                        .build();

        HttpClientUtil.runAsyncPost(Constants.REQUEST_PATH_READ_FILE, body, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(()->
                    appController.showPopUpMessage(e.getMessage()));
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try(ResponseBody responseBody = response.body()){
                    if (response.code() == 200) {
                        String[] responseStrings = constants.Constants.GSON_INSTANCE.fromJson(responseBody.string(),String[].class);
                        EngineDTO engine = constants.Constants.GSON_INSTANCE.fromJson(responseStrings[0], EngineDTO.class);
                        appController.setEngineDetails(engine);
                        DictionaryDTO dictionary = constants.Constants.GSON_INSTANCE.fromJson(responseStrings[1], DictionaryDTO.class);
                        appController.setDictionaryDetails(dictionary);
                        Platform.runLater(()->appController.setIsGoodFileSelected(true));
                    } else {
                        String body = responseBody.string();
                        Platform.runLater(()->{
                            appController.setSelectedFile("-");
                            appController.showPopUpMessage(response.code() + body);
                        });
                    }
                }
            }
        });
    }

    public void updateMachineConfiguration(MachineInfoDTO initialArgs) {

        String json = constants.Constants.GSON_INSTANCE.toJson(initialArgs);

        RequestBody body =
                new MultipartBody.Builder()
                        .addFormDataPart("file", json)
                        .build();

        HttpClientUtil.runAsyncPost(Constants.REQUEST_PATH_INIT_USER_MACHINE, body, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

                Platform.runLater(()->appController.showPopUpMessage("error: " + e.getMessage()));
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try(ResponseBody responseBody = response.body()) {
                    String body = responseBody.string();
                    Platform.runLater(()->{
                       if (response.code() == 200) {
                           codeSetEventHandler.fireEvent(constants.Constants.GSON_INSTANCE.fromJson(body, EngineDTO.class));
                       } else {
                           Platform.runLater(()->appController.showPopUpMessage(response.code() + " " + body));
                       }
                    });
                }
            }
        });
    }

    public void encryptInput(String message) {

        String finalUrl = HttpUrl.parse(Constants.REQUEST_PATH_ENCRYPT_MESSAGE).newBuilder()
                .addQueryParameter("message", message).build().toString();

        HttpClientUtil.runAsyncGet(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() -> appController.showPopUpMessage("error: " + e.getMessage()));
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    String body = responseBody.string();
                    Platform.runLater(() -> {
                        if (response.code() == 200) {
                            EngineWithEncryptDTO resMsg = constants.Constants.GSON_INSTANCE.fromJson(body, EngineWithEncryptDTO.class);
                            codeSetEventHandler.fireEvent(resMsg.getEngineDTO());
                            encryptedMessage.set(resMsg.getEncryptedMsg());
                        } else {
                            Platform.runLater(()->
                                appController.showPopUpMessage(response.code() + " " + body));
                        }
                    });
                }
            }
        });
    }

    public StringProperty encryptedMessageProperty() {
        return encryptedMessage;
    }

    public void logOut() {
        String finalUrl = HttpUrl.parse(constants.Constants.REQUEST_PATH_LOGOUT).newBuilder()
                .build().toString();

        HttpClientUtil.runAsyncGet(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("FAILURE --- the server continue to competing");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.code()!=200){
                    System.out.println("NOT LOGOUT WELL");
                } else {
                    Platform.runLater(()->appController.initialFileLoadable());
                    System.out.println("LOGOUT WORKS GREAT");
                }

                response.close();
            }
        });
    }

    public void finishContest(CandidateDataDTO winnerCandidate) {

        String finalUrl = HttpUrl.parse(Constants.REQUEST_PATH_FINISH_CONTEST).newBuilder()
                .addQueryParameter("message", winnerCandidate.getDecryptedMessage())
                .addQueryParameter("winnerTeam", winnerCandidate.getFoundersName())
                .addQueryParameter("configuration", winnerCandidate.getConfiguration().toString())
                .build().toString();

        HttpClientUtil.runAsyncGet(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("FAILURE --- the server continue to competing");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.code()!=200){
                    System.out.println("the server continue to competing");
                } else {
                    System.out.println("contest is over!!!!");
                }

                response.close();
            }
        });
    }
}
