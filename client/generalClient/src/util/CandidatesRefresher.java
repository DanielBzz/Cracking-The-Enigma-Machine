package util;

import constants.Constants;
import contestDtos.CandidateDataDTO;
import http.HttpClientUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.TimerTask;
import java.util.function.Consumer;


public class CandidatesRefresher extends TimerTask {

    private final Consumer<List<CandidateDataDTO>> updateCandidatesConsumer;
    private int version;

    public CandidatesRefresher(Consumer<List<CandidateDataDTO>> consumer,int version) {
        this.updateCandidatesConsumer = consumer;
        this.version = version;
    }

    @Override
    public void run() {

        String finalUrl = HttpUrl.parse(Constants.REQUEST_PATH_GET_CANDIDATES).newBuilder()
                .addQueryParameter("version", String.valueOf(version)).build().toString();

        HttpClientUtil.runAsync(Constants.REQUEST_PATH_GET_CANDIDATES, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("update candidates failed");

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                if(response.code() == 200){
                    String[] res = Constants.GSON_INSTANCE.fromJson(response.body().string(), String[].class);
                    if(version != Integer.parseInt(res[0])){
                        version = Integer.parseInt(res[0]);
                        CandidateDataDTO[] newCandidates = Constants.GSON_INSTANCE.fromJson(res[1], CandidateDataDTO[].class);
                        updateCandidatesConsumer.accept(Arrays.asList(newCandidates));
                    }
                }else {
                    System.out.println(response.code() + "update candidates failed");
                }
            }
        });
    }
}
