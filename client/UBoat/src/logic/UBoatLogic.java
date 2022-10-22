package logic;

import components.main.UBoatMainAppController;
import decryptionDtos.DictionaryDTO;
import http.HttpClientUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import logic.events.CodeSetEventListener;
import logic.events.handler.MachineEventHandler;
import machineDtos.EngineDTO;
import machineDtos.EngineWithEncryptDTO;
import machineDtos.MachineInfoDTO;
import okhttp3.*;
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

        RequestBody body =
                new MultipartBody.Builder()
                        .addFormDataPart("file", f.getName(), RequestBody.create(f, MediaType.parse("text/plain")))
                        .build();

        try (Response res = HttpClientUtil.runPost(Constants.REQUEST_PATH_READ_FILE, body)) {

            if (res.code() == 200) {
                String[] responseStrings = constants.Constants.GSON_INSTANCE.fromJson(res.body().string(),String[].class);
                EngineDTO engine = constants.Constants.GSON_INSTANCE.fromJson(responseStrings[0], EngineDTO.class);
                appController.setEngineDetails(engine);
                DictionaryDTO dictionary = constants.Constants.GSON_INSTANCE.fromJson(responseStrings[1], DictionaryDTO.class);
                appController.setDictionaryDetails(dictionary);
                appController.setIsGoodFileSelected(true);
            } else {
                appController.setSelectedFile("-");
                appController.showPopUpMessage(res.code() + res.body().string());
            }
        } catch (IOException e) {
            appController.showPopUpMessage(e.getMessage());
        }
    }

    public void updateMachineConfiguration(MachineInfoDTO initialArgs) {

        String json = constants.Constants.GSON_INSTANCE.toJson(initialArgs);

        RequestBody body =
                new MultipartBody.Builder()
                        .addFormDataPart("file", json)
                        .build();

        try (Response res = HttpClientUtil.runPost(Constants.REQUEST_PATH_INIT_USER_MACHINE, body)) {

            if (res.code() == 200) {
                codeSetEventHandler.fireEvent(constants.Constants.GSON_INSTANCE.fromJson(res.body().string(), EngineDTO.class));
            } else {
                appController.showPopUpMessage(res.code() + " " + res.body().string());
            }
        } catch (Exception e) {
            appController.showPopUpMessage("error: " + e.getMessage());
            e.printStackTrace();
        }

    }

    public void encryptInput(String message) {

        String finalUrl = HttpUrl.parse(Constants.REQUEST_PATH_ENCRYPT_MESSAGE).newBuilder()
                .addQueryParameter("message", message).build().toString();

        try (Response res = HttpClientUtil.runGet(finalUrl)) {

            if (res.code() == 200) {
                EngineWithEncryptDTO resMsg = constants.Constants.GSON_INSTANCE.fromJson(res.body().string(), EngineWithEncryptDTO.class);
                codeSetEventHandler.fireEvent(resMsg.getEngineDTO());
                encryptedMessage.set(resMsg.getEncryptedMsg());
            } else {
                appController.showPopUpMessage(res.code() + " " + res.body().string());
            }

        } catch (IOException e) {
            appController.showPopUpMessage("error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public StringProperty encryptedMessageProperty() {
        return encryptedMessage;
    }

    public void logOut() {
    }
}
