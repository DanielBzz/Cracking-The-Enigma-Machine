package util;

import constants.Constants;
import contestDtos.ActivePlayerDTO;
import http.HttpClientUtil;
import javafx.application.Platform;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.TimerTask;
import java.util.function.Consumer;

public class ConnectedUserListRefresher extends TimerTask {

    private final Consumer<List<ActivePlayerDTO>> userListConsumer;

    public ConnectedUserListRefresher(Consumer<List<ActivePlayerDTO>> userListConsumer) {
        this.userListConsumer = userListConsumer;
    }


    @Override
    public void run() {

        HttpClientUtil.runAsync(Constants.REQUEST_PATH_USERS_UPDATE, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("update user list failed");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                if(response.code() == 200){
                    ActivePlayerDTO[] users = Constants.GSON_INSTANCE.fromJson(response.body().string(),ActivePlayerDTO[].class);
                    Platform.runLater(
                            ()->userListConsumer.accept(Arrays.asList(users)));
                }
                else {
                    System.out.println(response.code() + " update user list failed, should be redirect here");
                }
            }
        });
    }
}
