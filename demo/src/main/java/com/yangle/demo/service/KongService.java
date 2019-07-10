package com.yangle.demo.service;

import com.alibaba.fastjson.JSONObject;
import com.yangle.demo.model.KongRes;
import com.yangle.demo.model.ResultVO;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.Base64;

@Service
public class KongService {


    private static String KONG_ADMIN_API_URL = "http://10.0.45.172:30991";
    private static String PLUGINS_URI = "/plugins";

    public Boolean changeZServiceStatus(Integer status) {
        // 获取plugins 列表


        return true;
    }

    private static KongRes getPluginList() {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

        HttpMethod method = HttpMethod.GET;
        HttpEntity<JSONObject> requestEntity = new HttpEntity<>(headers);

        RestTemplate client = new RestTemplate();
        ResponseEntity<KongRes> response = client.exchange(KONG_ADMIN_API_URL + PLUGINS_URI, method,
                requestEntity, KongRes.class);

        return response.getBody();
    }

    public static void main(String[] args) {
        String jwt = Jwts.builder()
                .setHeaderParam("typ", "jwt")
                .setHeaderParam("alg", "HS256")
                .setIssuer("iss")
                .signWith(SignatureAlgorithm.HS256, Base64.getEncoder().encodeToString("YmxvYiBkYXRh".getBytes(Charset.forName("utf-8"))))
                .compact();
    }

}
