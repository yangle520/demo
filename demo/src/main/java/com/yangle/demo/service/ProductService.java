package com.yangle.demo.service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.yangle.demo.model.ResultVO;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;


@Service
public class ProductService {

    private static String dir = System.getProperty("user.dir");
    private static String ip = "http://10.0.45.173:30212";
    private static String url = ip + "/pco/v1/product/instance-id";

    private static boolean pFlag = false;

    public String getInstanceId(String namespace, String cloudName) {
        JSONObject params = new JSONObject();
        params.put("nameSpace", namespace);
        params.put("cloudName", cloudName);
        ResultVO res = httpPost(url, params);
        return res != null && res.getRes() != null ? String.valueOf(res.getRes()) : "";
    }

    public void start() {
        pFlag = true;
        CompletableFuture.supplyAsync(this::getInstanceId);
        CompletableFuture.supplyAsync(this::getInstanceId);
        CompletableFuture.supplyAsync(this::getInstanceId);
        CompletableFuture.supplyAsync(this::getInstanceId);
        CompletableFuture.supplyAsync(this::getInstanceId);
    }

    public void stop() {
        pFlag = false;
    }

    private boolean getInstanceId() {
        String fileName = dir + "instance-id-" + Thread.currentThread().getName() + ".log";
        File newFile = new File(fileName);
        int i = 0;
        while (pFlag) {
            String id = getInstanceId("ns", "cn");
            try {
                Files.append(id + "\r\n", newFile, Charsets.UTF_8);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (i++ > 1000000) {
                break;
            }
        }
        return true;
    }

    private static ResultVO httpPost(String url, JSONObject params) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

        HttpMethod method = HttpMethod.POST;
        HttpEntity<JSONObject> requestEntity = new HttpEntity<>(params, headers);

        RestTemplate client = new RestTemplate();
        ResponseEntity<ResultVO> response = client.exchange(url, method, requestEntity, ResultVO.class);

        return response.getBody();
    }

}
