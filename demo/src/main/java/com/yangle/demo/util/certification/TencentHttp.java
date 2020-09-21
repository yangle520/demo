package com.yangle.demo.util.certification;

import okhttp3.*;

import java.io.IOException;

public class TencentHttp {

    private static final MediaType    MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");
    private static       OkHttpClient client     = new OkHttpClient();

    static Response post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(MEDIA_TYPE, json);
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response;
    }

    static Response get(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();
        return response;
    }
}
