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

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Enumeration;
import java.util.UUID;

import static com.yangle.demo.util.SignatureUtil.HmacSHA1Encrypt;

@RestController
@Slf4j
public class HelloController {


    @GetMapping(value = "/ecs", params = "Action=DescribeInstances")
    public String DescribeInstances(HttpServletRequest request,
                                    @RequestHeader(name = "iam-user-id", required = false) String userId,
                                    @RequestHeader(name = "iam-user-name", required = false) String userName,
                                    @RequestHeader(name = "iam-user-type", required = false) String userType,
                                    @RequestHeader(name = "iam-parent-id", required = false) String parentId,
                                    @RequestHeader(name = "iam-parent-name", required = false) String parentName,
                                    @RequestHeader(name = "iam-policy", required = false) String policy) {
        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String paramName = (String) headerNames.nextElement();
            if (paramName.startsWith("iam-")) {
                String paramValue = request.getHeader(paramName);
                log.info("{}:{}", paramName, paramValue);
            }
        }
        return "123";
    }

    @PostMapping(value = "", params = "Action=DeleteDBInstance")
    public RestfulEntity<Long> hello(@RequestHeader(name = "x-user-id", required = false) String userId,
                                     @RequestHeader(name = "RequestId", required = false) String requestId,
                                     @RequestParam(name = "name", required = false) String name,
                                     @RequestParam(name = "id", required = false) String id) {

        log.info("userId:{},requestId:{}", userId, requestId);
        return new RestfulEntity(true, true, userId, 12345678912345l, "abcd");
    }


    @RequestMapping("/a")
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
