package com.yangle.demo.openapi.aliyun;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.ecs.model.v20140526.DescribeInstancesRequest;
import com.aliyuncs.ecs.model.v20140526.DescribeInstancesResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.profile.DefaultProfile;
import com.google.common.base.Joiner;
import com.yangle.demo.openapi.model.ECSRequest;
import com.yangle.demo.openapi.model.ECSResponse;
import com.yangle.demo.openapi.BaseOpenApi;
import com.yangle.demo.openapi.OpenApiRequest;
import com.yangle.demo.openapi.OpenApiResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.stream.Collectors;

@Slf4j
public class DescribeInstances extends BaseOpenApi {

    @Override
    public OpenApiResponse doAction(OpenApiRequest request) {
        // 参数转成具体类型
        ECSRequest req = (ECSRequest) request;

        try {
            // 构造请求参数
            DescribeInstancesRequest describeInstancesRequest = new DescribeInstancesRequest();
            describeInstancesRequest.setInstanceIds(req.getInstanceId());
            describeInstancesRequest.setInstanceName(req.getInstanceName());

            // 创建连接client
            DefaultProfile profile = DefaultProfile.getProfile(request.getRegionId(), req.getAccessKeyId(), req.getAccessSecret());
            IAcsClient client = new DefaultAcsClient(profile);

            DescribeInstancesResponse response = client.getAcsResponse(describeInstancesRequest);
            log.info("DescribeInstancesResponse:{}", response);

            // 响应结果处理
            ECSResponse res = new ECSResponse();
            res.setRequestId(response.getRequestId());
            res.setTotalCount(response.getTotalCount());
            res.setPageSize(response.getPageSize());
            res.setPageNumber(response.getPageNumber());

            if (response.getInstances() != null && response.getInstances().size() > 0) {
                res.setDetails(response.getInstances().stream().map(o -> {
                    String instanceName = o.getInstanceName();
                    String publicIP = Joiner.on(",").skipNulls().join(o.getPublicIpAddress());
                    String innerIP = Joiner.on(",").skipNulls().join(o.getInnerIpAddress());
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
        } catch (ServerException e) {
            return ECSResponse.buildByAliServerException(e);
        } catch (ClientException e) {
            return ECSResponse.buildByAliClientException(e);
        }
    }


}
