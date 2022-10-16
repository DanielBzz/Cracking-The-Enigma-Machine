package util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import contestDtos.CandidateDataDTO;
import http.HttpClientUtil;
import javafx.beans.property.BooleanProperty;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.TimerTask;
import java.util.function.Consumer;


public class CandidatesRefresher extends TimerTask {

    private final String answerConsumer;
    private final Consumer<CandidateDataDTO> httpRequestLoggerConsumer;
    private final BooleanProperty shouldUpdate;

    public CandidatesRefresher(String answerConsumer, Consumer<CandidateDataDTO> httpRequestLoggerConsumer, BooleanProperty shouldUpdate) {
        this.answerConsumer = answerConsumer;
        this.httpRequestLoggerConsumer = httpRequestLoggerConsumer;
        this.shouldUpdate = shouldUpdate;
    }

    @Override
    public void run() {
        if (!shouldUpdate.get()) {
            return;
        }

        //httpRequestLoggerConsumer.accept("About to invoke: " + Constants.USERS_LIST + " | Users Request # " + finalRequestNumber);

        HttpClientUtil.runAsync(answerConsumer, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                //httpRequestLoggerConsumer.accept("Users Request # " + finalRequestNumber + " | Ended with failure...");
                //send error message
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(CandidateDataDTO.class, new CandidateDeserializer())
                        .create();

                CandidateDataDTO jsonArrayOfCandidates = gson.fromJson(response.body().string(), CandidateDataDTO.class);
                httpRequestLoggerConsumer.accept(jsonArrayOfCandidates);
            }
        });
    }
}
