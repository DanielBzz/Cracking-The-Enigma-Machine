package util;

import http.HttpClientUtil;
import javafx.application.Platform;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.TimerTask;
import java.util.function.Consumer;

public class UsersListRefresher extends TimerTask {

    private final String url;
    private final Consumer<String> userListConsumer;

    public UsersListRefresher(Consumer<String> userListConsumer, String url) {
        this.userListConsumer = userListConsumer;
        this.url = url;
    }

    @Override
    public void run() {

        HttpClientUtil.runAsyncGet(url, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("update user list failed");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                try(ResponseBody responseBody = response.body()){
                    if(response.code() == 200){
                        String res = responseBody.string();

                        Platform.runLater(()->userListConsumer.accept(res));
                        System.out.println("responseBody of UserListRefresher is: " + res);
                    }
                    else {
                        System.out.println(response.code() + "  " +response.body().string());
                    }
                }
            }
        });
    }
}