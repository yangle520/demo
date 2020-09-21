package com.yangle.demo.controller;


import com.alibaba.fastjson.JSONArray;
import com.yangle.demo.openapi.model.ECSRequest;
import com.yangle.demo.openapi.OpenApiFactory;
import com.yangle.demo.openapi.OpenApiRequest;
import com.yangle.demo.openapi.OpenApiResponse;
import com.yangle.demo.openapi.model.RegionRequest;
import com.yangle.demo.openapi.model.RegionResponse;
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

    @RequestMapping("/query/region")
    public JSONArray queryRegion() {

        // 查询用户绑定了几个云厂商

        RegionRequest req1 = new RegionRequest();
        setAK("", "aliyun", req1);
        OpenApiResponse res1 = OpenApiFactory.callOpenApi("DescribeRegions", "aliyun", req1);

        RegionRequest req2 = new RegionRequest();
        setAK("", "tencent", req2);
        OpenApiResponse res2 =  OpenApiFactory.callOpenApi("DescribeRegions", "tencent", req2);

        // 合并
        JSONArray array = new JSONArray();
        array.add(res1);
        array.add(res2);

        return array;
    }


    private void setAK(String userId, String type, OpenApiRequest request) {
        if ("aliyun".equals(type)) {
            request.setAccessKeyId("");
            request.setAccessSecret("");
        } else if ("tencent".equals(type)) {
            request.setAccessKeyId("");
            request.setAccessSecret("");
        }
    }

}
