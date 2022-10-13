package util;

import decryptionDtos.AgentAnswerDTO;
import javafx.beans.property.BooleanProperty;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import util.http.HttpClientUtil;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.TimerTask;
import java.util.function.Consumer;

import static util.Constants.GSON_INSTANCE;

public class CandidatesRefresher extends TimerTask {

    private final String answerConsumer;
    private final Consumer<String> httpRequestLoggerConsumer;
    private final BooleanProperty shouldUpdate;

    public CandidatesRefresher(String answerConsumer, Consumer<String> httpRequestLoggerConsumer, BooleanProperty shouldUpdate) {
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
                String jsonArrayOfUsersNames = response.body().string();
                httpRequestLoggerConsumer.accept(jsonArrayOfUsersNames);
            }
        });
    }
}
