package util;

import constants.Constants;
import http.HttpClientUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.TimerTask;
import java.util.function.Consumer;

public class ConnectedUserListRefresher extends TimerTask {

    private final Consumer<String> userListConsumer;

    public ConnectedUserListRefresher(Consumer<String> userListConsumer) {
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
                    userListConsumer.accept(response.body().string());
                }
                else {
                    System.out.println(response.code() + " update user list failed, should be redirect here");
                }
            }
        });
    }
}
