package com.yangle.demo.openapi.tencent;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Joiner;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.cvm.v20170312.CvmClient;
import com.tencentcloudapi.cvm.v20170312.models.DescribeInstancesRequest;
import com.tencentcloudapi.cvm.v20170312.models.DescribeInstancesResponse;
import com.yangle.demo.openapi.model.ECSRequest;
import com.yangle.demo.openapi.model.ECSResponse;
import com.yangle.demo.openapi.BaseOpenApi;
import com.yangle.demo.openapi.OpenApiRequest;
import com.yangle.demo.openapi.OpenApiResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
public class DescribeInstances extends BaseOpenApi {

    @Override
    public OpenApiResponse doAction(OpenApiRequest request) {
        ECSRequest req = (ECSRequest) request;

        try {
            // 构造请求参数
            JSONObject paramJson = new JSONObject();
            JSONArray filters = new JSONArray();

            if (req.getInstanceName() != null) {
                JSONObject json = new JSONObject();
                json.put("Name", "instance-name");
                json.put("Values", new String[]{req.getInstanceName()});
                filters.add(json);
            }
            if (filters.size() > 0) {
                paramJson.put("Filters", filters);
            }
            String params = paramJson.toJSONString();
            DescribeInstancesRequest describeInstancesRequest = DescribeInstancesRequest.fromJsonString(params, DescribeInstancesRequest.class);

            // 创建连接client
            Credential cred = new Credential(req.getAccessKeyId(), req.getAccessSecret());
            CvmClient client = new CvmClient(cred, req.getRegionId());

            // 发生请求
            DescribeInstancesResponse response = client.DescribeInstances(describeInstancesRequest);
            log.info("DescribeInstancesResponse:{}", response);

            // 响应结果处理
            ECSResponse res = new ECSResponse();
            res.setTotalCount(response.getTotalCount());
            res.setRequestId(response.getRequestId());

            if (response.getInstanceSet() != null && response.getInstanceSet().length > 0) {
                res.setDetails(Arrays.stream(response.getInstanceSet()).map(o -> {
                    String instanceName = o.getInstanceName();
                    String publicIP = Joiner.on(",").skipNulls().join(o.getPublicIpAddresses());
                    String innerIP = Joiner.on(",").skipNulls().join(o.getPrivateIpAddresses());
                    String expiredTime = o.getExpiredTime();

                    return ECSResponse.ECSInfo.builder()
                            .instanceName(instanceName)
                            .publicIP(publicIP)
                            .innerIP(innerIP)
                            .expireTime(expiredTime)
                            .build();
                }).collect(Collectors.toList()));
            }

            return res;

        } catch (TencentCloudSDKException e) {
            return ECSResponse.buildByTencentCloudSDKException(e);
        }


    }

}
