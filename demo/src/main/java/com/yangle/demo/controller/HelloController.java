package com.yangle.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.hash.Hashing;
import com.yangle.demo.model.RestfulEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.UUID;

import static com.yangle.demo.util.AKTestUtil.HmacSHA1Encrypt;

@RestController
@Slf4j
public class HelloController {


    @GetMapping(params = "Action=CreateDBInstance")
    public void CreateDBInstance(@RequestHeader(name = "x-user-id", required = false) String userId,
                                 @RequestHeader(name = "RequestId", required = false) String requestId) {
        log.info("userId:{},requestId:{}", userId, requestId);
    }

    @PostMapping(params = "Action=DeleteDBInstance")
    public RestfulEntity<Long> hello(@RequestHeader(name = "x-user-id", required = false) String userId,
                                     @RequestHeader(name = "RequestId", required = false) String requestId,
                                     @RequestParam(name = "name", required = false) String name,
                                     @RequestParam(name = "id", required = false) String id) {

        log.info("userId:{},requestId:{}", userId, requestId);
        return new RestfulEntity(true, true, userId, 12345678912345l, "abcd");
    }


    @RequestMapping("/a/b/b")
    public String abb() {
        return "abb";
    }

    @RequestMapping("/a/a")
    public String aa() {
        return "aa";
    }

    public static void main(String[] args) throws Exception {
        String url = "http://10.0.6.73:30990/yl?Action=DeleteDBInstance";
        String ak = "3gl7Dpffn5AM0vrm";
        String aks = "wp1eZtqX3iPxfCQSDuJSateF0lb5Ss";

        JSONObject requestBody = new JSONObject();
        {
            requestBody.put("scenes", Lists.newArrayList("porn"));
            JSONObject task = new JSONObject();
            task.put("dataId", "test2NInmO$tAON6qYUrtCRgLo-1mwxdi");
            task.put("url", "https://img.alicdn.com/tfs/TB1urBOQFXXXXbMXFXXXXXXXXXX-1442-257.png");
            requestBody.put("task", task);
        }

        String bodyMD5 = Hashing.md5().hashString(requestBody.toJSONString(), Charsets.UTF_8).toString();
        System.out.println("body md5:" + bodyMD5);
        Date date = new Date();
        String uuid = UUID.randomUUID().toString();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Lists.newArrayList(MediaType.APPLICATION_JSON));
        headers.add("x-uco-version", "2019-08-08");
        headers.add("x-uco-signature-nonce", uuid);
        headers.add("x-uco-signature-version", "1.0");
        headers.add("x-uco-signature-method", "HMAC-SHA1");
        headers.add("Content-MD5", bodyMD5);
        headers.add("Date", String.valueOf(date));

        String stringToSign = "POST\napplication/json\n" +
                bodyMD5 + "\n" +
                "application/json" + "\n" +
                date + "\n" +
                "x-uco-signature-method:HMAC-SHA1" + "\n" +
                "x-uco-signature-nonce:" + uuid + "\n" +
                "x-uco-signature-version:1.0" + "\n" +
                "x-uco-version:2019-08-08" + "\n";

        String signature = HmacSHA1Encrypt(stringToSign, aks);
        System.out.println(stringToSign);
        System.out.println(signature);

        headers.add("Authorization", "uco " + ak + ":" + signature);

        HttpMethod method = HttpMethod.POST;
        HttpEntity<JSONObject> requestEntity = new HttpEntity<>(requestBody, headers);

        RestTemplate client = new RestTemplate();
        ResponseEntity<JSONObject> response = client.exchange(url, method, requestEntity, JSONObject.class);
        System.out.println(response.getBody());
    }

}
