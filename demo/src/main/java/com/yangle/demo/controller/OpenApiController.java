package com.yangle.demo.controller;


import com.yangle.demo.openapi.model.ECSRequest;
import com.yangle.demo.openapi.OpenApiFactory;
import com.yangle.demo.openapi.OpenApiRequest;
import com.yangle.demo.openapi.OpenApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/openapi")
public class OpenApiController {

    @RequestMapping("/query/instances")
    public OpenApiResponse queryInstance(@RequestParam(name = "type") String type, ECSRequest req) {

        // 根据userId和类型查询绑定的 ak/sk
        setAK("", type, req);

        // 调用对应方法
        return OpenApiFactory.callOpenApi("DescribeInstances", type, req);
    }



    private void setAK(String userId, String type, OpenApiRequest request) {
        if ("aliyun".equals(type)) {
            request.setAccessKeyId("LTAIi2mioTi2P8ZA");
            request.setAccessSecret("t2qZHjedax8f6QQjGzeQP4vFmgDvzq");
        } else if ("tencent".equals(type)) {
            request.setAccessKeyId("AKIDkKgomWtIKBnBMJ9VklX4NRL1SfeOqBD0");
            request.setAccessSecret("g8LK5bsyqTr0vb3HH2YqwoStBF6i5E8n");
        }
    }
}
