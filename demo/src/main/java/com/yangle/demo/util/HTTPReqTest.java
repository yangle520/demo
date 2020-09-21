package com.yangle.demo.util;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class HTTPReqTest {


    public static void main(String[] args) throws Exception {
        Long start = System.currentTimeMillis();


        CompletableFuture<Boolean> a1 = CompletableFuture.supplyAsync(() -> paresIP());
        CompletableFuture<Boolean> a2 = CompletableFuture.supplyAsync(() -> paresIP());
        CompletableFuture<Boolean> a3 = CompletableFuture.supplyAsync(() -> paresIP());

        a1.join();
        a2.join();
        a3.join();

        Long end = System.currentTimeMillis();
        System.out.println((end - start));
    }

    private static boolean paresIP() {
        for (int i = 0; i < 100; i++) {
            try {
                String url = "http://ip-api.com/json/115.191.200.34?lang=zh-CN";
                HttpGet httpGet = new HttpGet(url);
                CloseableHttpClient client = HttpClients.createDefault();
                CloseableHttpResponse response = client.execute(httpGet);
                HttpEntity entity = response.getEntity();
                String result = EntityUtils.toString(entity);

                System.out.println(result);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

}
