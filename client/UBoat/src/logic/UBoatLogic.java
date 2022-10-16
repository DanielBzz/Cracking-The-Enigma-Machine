package logic;

import components.main.UBoatMainAppController;
import http.HttpClientUtil;
import logic.events.CodeSetEventListener;
import logic.events.handler.MachineEventHandler;
import machineDtos.EngineDTO;
import machineDtos.MachineInfoDTO;
import okhttp3.*;
import util.Constants;

import java.io.File;
import java.io.IOException;


public class UBoatLogic {

    private final UBoatMainAppController appController;
    public MachineEventHandler<CodeSetEventListener> codeSetEventHandler = new MachineEventHandler<>();


    public UBoatLogic(UBoatMainAppController appController){

        this.appController = appController;
    }

    public void uploadFileToServer(String filePath) {

        File f = new File(filePath);
        if(!f.exists()){
            return;
        }

        RequestBody body =
                new MultipartBody.Builder()
                        .addFormDataPart("file",f.getName(), RequestBody.create(f, MediaType.parse("text/plain")))
                        .build();

        try(Response res = HttpClientUtil.runPost(Constants.REQUEST_PATH_READ_FILE, body)){

            if(res.code() == 200){
                EngineDTO engine = constants.Constants.GSON_INSTANCE.fromJson(res.body().string(), EngineDTO.class);
                appController.setEngineDetails(engine);
                appController.setIsGoodFileSelected(true);
            }
            else{
                appController.setSelectedFile("-");
                appController.showPopUpMessage(res.code() + res.body().string() );
            }
        } catch (IOException e) {
            appController.showPopUpMessage(e.getMessage());
        }
    }

    public void updateMachineConfiguration(MachineInfoDTO initialArgs){

        String json = constants.Constants.GSON_INSTANCE.toJson(initialArgs);

        RequestBody body =
                new MultipartBody.Builder()
                        .addFormDataPart("file", json)
                        .build();

        try(Response res = HttpClientUtil.runPost(Constants.REQUEST_PATH_INIT_USER_MACHINE ,body)) {

            if(res.code() == 200){
                codeSetEventHandler.fireEvent(constants.Constants.GSON_INSTANCE.fromJson(res.body().string(),EngineDTO.class));
            }else {
                appController.showPopUpMessage(res.code() + " " + res.body().string());
            }
        }catch (Exception e) {
            appController.showPopUpMessage("error: " + e.getMessage());
            e.printStackTrace();
        }

    }

    public String encryptInput(String message) throws IOException{

        HttpUrl.Builder urlBuilder = HttpUrl.parse(Constants.REQUEST_PATH_ENCRYPT_MESSAGE).newBuilder();
        urlBuilder.addQueryParameter("message", message);
        String finalUrl = urlBuilder.build().toString();
        //System.out.println("About to invoke URL: [" + finalUrl + "]");

        Request request = new Request.Builder()
                .url(finalUrl)
                .build();

        Call call = Constants.HTTP_CLIENT.newCall(request);

        return call.execute().message();
    }
}
