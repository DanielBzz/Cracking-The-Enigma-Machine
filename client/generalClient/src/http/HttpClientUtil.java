package http;

import okhttp3.*;

import java.io.IOException;

public class HttpClientUtil {

    private final static OkHttpClient HTTP_CLIENT =
            new OkHttpClient.Builder()
                    .cookieJar(new SimpleCookieManager())
                    .followRedirects(false)
                    .build();

    public static void runAsync(String finalUrl, Callback callback) {
        Request request = new Request.Builder()
                .url(finalUrl)
                .build();

        Call call = HttpClientUtil.HTTP_CLIENT.newCall(request);


        call.enqueue(callback);
    }

    public static Response runPost(String url, RequestBody body) throws IOException {

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        return HTTP_CLIENT.newCall(request).execute();
    }

    public static Response runGet(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        return HTTP_CLIENT.newCall(request).execute();
    }

    public static void shutdown() {
        System.out.println("Shutting down HTTP CLIENT");
        HTTP_CLIENT.dispatcher().executorService().shutdown();
        HTTP_CLIENT.connectionPool().evictAll();
    }

    public static void removeCookiesOf(String domain) {
        ((SimpleCookieManager)HTTP_CLIENT.cookieJar()).removeCookiesOf(domain);
    }
}
