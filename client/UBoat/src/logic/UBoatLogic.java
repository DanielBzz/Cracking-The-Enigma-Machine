package logic;

import com.google.gson.Gson;
import machineDtos.EnigmaMachineDTO;
import okhttp3.*;
import util.Constants;

import java.io.File;
import java.io.IOException;


public class UBoatLogic {

    // list of allies

    public void uploadFileToServer(String filePath, String fileName) throws IOException {

        File f = new File(filePath);

        RequestBody body =
                new MultipartBody.Builder()
                        .addFormDataPart(fileName, f.getName(), RequestBody.create(f, MediaType.parse("text/plain")))
                        .build();

        Request request = new Request.Builder()
                .url(Constants.REQUEST_PATH_READ_FILE)
                .post(body)
                .build();

        Call call = Constants.HTTP_CLIENT.newCall(request);

        Response response = call.execute();

        System.out.println(response.body().string());
    }

    public void updateMachineConfiguration(EnigmaMachineDTO initialArgs) throws IOException{

        Gson gson = new Gson();

        Request request = new Request.Builder()
                .url(Constants.REQUEST_PATH_INIT_USER_MACHINE)
                .post(RequestBody.create(gson.toJson(initialArgs).getBytes()))
                .build();

        Call call = Constants.HTTP_CLIENT.newCall(request);

        Response response = call.execute();
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
