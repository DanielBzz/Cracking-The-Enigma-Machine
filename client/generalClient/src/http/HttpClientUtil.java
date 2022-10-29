package http;

import okhttp3.*;

public class HttpClientUtil {

    private final static OkHttpClient HTTP_CLIENT =
            new OkHttpClient.Builder()
                    .cookieJar(new SimpleCookieManager())
                    .followRedirects(false)
                    .build();

    public static void runAsyncGet(String finalUrl, Callback callback) {
        Request request = new Request.Builder()
                .url(finalUrl)
                .build();

        Call call = HttpClientUtil.HTTP_CLIENT.newCall(request);


        call.enqueue(callback);
    }

    public static void runAsyncPost(String url, RequestBody body, Callback callback){

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        Call call = HttpClientUtil.HTTP_CLIENT.newCall(request);
        call.enqueue(callback);
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
