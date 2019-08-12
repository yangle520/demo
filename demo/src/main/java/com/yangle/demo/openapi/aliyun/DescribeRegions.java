package com.yangle.demo.openapi.aliyun;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.profile.DefaultProfile;
import com.google.gson.Gson;
import com.aliyuncs.ecs.model.v20140526.*;
import com.yangle.demo.openapi.BaseOpenApi;
import com.yangle.demo.openapi.OpenApiRequest;
import com.yangle.demo.openapi.OpenApiResponse;
import com.yangle.demo.openapi.model.ECSResponse;
import com.yangle.demo.openapi.model.RegionRequest;
import com.yangle.demo.openapi.model.RegionResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.stream.Collectors;

@Slf4j
public class DescribeRegions extends BaseOpenApi {


    @Override
    public OpenApiResponse doAction(OpenApiRequest openApiRequest) {
        RegionRequest req = (RegionRequest) openApiRequest;

        try {
            DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", req.getAccessKeyId(), req.getAccessSecret());

            IAcsClient client = new DefaultAcsClient(profile);

            DescribeRegionsRequest request = new DescribeRegionsRequest();


            DescribeRegionsResponse response = client.getAcsResponse(request);
            log.info("DescribeRegionsResponse:{}", JSONObject.toJSONString(response));

            RegionResponse res = new RegionResponse();
            res.setType("aliyun");
            res.setDetails(response.getRegions().stream().map(o -> new RegionResponse.RegionDetail(o.getBizRegionId(), o.getLocalName())).collect(Collectors.toList()));
            return res;
        } catch (ServerException e) {
            return ECSResponse.buildByAliServerException(e);
        } catch (ClientException e) {
            return ECSResponse.buildByAliClientException(e);
        }
    }

    public static void main(String[] args) {
        DescribeRegions d = new DescribeRegions();
        RegionRequest req = new RegionRequest();
        req.setAccessKeyId("LTAIF4pQ5PBUgBiGa");
        req.setAccessSecret("BIamRCTIyvUrTumpTNBS7QZWzjxj7O");
        System.out.println(d.doAction(req));
        ;
    }
}
