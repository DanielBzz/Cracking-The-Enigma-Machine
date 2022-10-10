package logic;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import machineDtos.EnigmaMachineDTO;
import okhttp3.*;
import thirdParty.json.MachineInfoDTOSerializer;

import java.io.File;
import java.io.IOException;

import static chat.util.Constants.*;

public class UBoatLogic {

    EnigmaSystemEngine engine;
    // list of allies







    public void uploadFileToServer(String filePath, String fileName) throws IOException {

        String RESOURCE = "/loadFile";

        File f = new File(filePath);

        RequestBody body =
                new MultipartBody.Builder()
                        .addFormDataPart(fileName, f.getName(), RequestBody.create(f, MediaType.parse("text/plain")))
                        .build();

        Request request = new Request.Builder()
                .url(UBOAT_REQUEST_PATH + RESOURCE)
                .post(body)
                .build();

        Call call = HTTP_CLIENT.newCall(request);

        Response response = call.execute();

        System.out.println(response.body().string());
    }


    public void updateMachineConfiguration(EnigmaMachineDTO initialArgs) throws IOException{
        String RESOURCE = "/chargeMachine";

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(EnigmaMachineDTO.class, new MachineInfoDTOSerializer())
                .create();

        Request request = new Request.Builder()
                .url(UBOAT_REQUEST_PATH + RESOURCE)
                .post(RequestBody.create(gson.toJson(initialArgs).getBytes()))
                .build();

        Call call = HTTP_CLIENT.newCall(request);

        Response response = call.execute();
    }
}
