package util;

import constants.Constants;
import contestDtos.CandidateDataDTO;
import http.HttpClientUtil;
import javafx.application.Platform;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.TimerTask;
import java.util.function.Consumer;


public class CandidatesRefresher extends TimerTask {

    private final Consumer<List<CandidateDataDTO>> updateCandidatesConsumer;
    private final Consumer<String> finishContestConsumer;
    private int version;

    public CandidatesRefresher(Consumer<List<CandidateDataDTO>> candidatesConsumer,Consumer<String> finishContestConsumer,int version) {
        this.updateCandidatesConsumer = candidatesConsumer;
        this.finishContestConsumer = finishContestConsumer;
        this.version = version;
    }

    @Override
    public void run() {

        String finalUrl = HttpUrl.parse(Constants.REQUEST_PATH_GET_CANDIDATES).newBuilder()
                .addQueryParameter("version", String.valueOf(version)).build().toString();

        HttpClientUtil.runAsyncGet(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("update candidates failed");

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                try(ResponseBody responseBody = response.body()){
                    String resBodyString = response.body().string();
                    if(response.code() == 200){
                        String[] res = Constants.GSON_INSTANCE.fromJson(resBodyString, String[].class);
                        if(version != Integer.parseInt(res[0])){
                            version = Integer.parseInt(res[0]);
                            CandidateDataDTO[] newCandidates = Constants.GSON_INSTANCE.fromJson(res[1], CandidateDataDTO[].class);
                            updateCandidatesConsumer.accept(Arrays.asList(newCandidates));
                        }

                        System.out.println(
                                "Could not response well, " +response.code() +": "+ resBodyString +",  url:" + finalUrl);
                    } else if (response.code() == 202) {
                        cancel();
                        Platform.runLater(()->finishContestConsumer.accept(resBodyString));

                        System.out.println(
                                "response well, " +response.code() +": "+ response.body().string() +",  url:" + finalUrl);
                    } else {
                        System.out.println(
                                "Could not response well, " +response.code() +": "+ resBodyString +",  url:" + finalUrl);
                    }
                }
            }
        });
    }
}
