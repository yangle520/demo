package com.yangle.demo.openapi.tencent;

import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.cvm.v20170312.CvmClient;
import com.tencentcloudapi.cvm.v20170312.models.DescribeRegionsRequest;
import com.tencentcloudapi.cvm.v20170312.models.DescribeRegionsResponse;
import com.yangle.demo.openapi.BaseOpenApi;
import com.yangle.demo.openapi.OpenApiRequest;
import com.yangle.demo.openapi.OpenApiResponse;
import com.yangle.demo.openapi.model.ECSResponse;
import com.yangle.demo.openapi.model.RegionRequest;
import com.yangle.demo.openapi.model.RegionResponse;

import java.util.Arrays;
import java.util.stream.Collectors;

public class DescribeRegions extends BaseOpenApi {

    @Override
    public OpenApiResponse doAction(OpenApiRequest openApiRequest) {
        RegionRequest req = new RegionRequest();

        try {
            Credential cred = new Credential(req.getAccessKeyId(), req.getAccessSecret());
            CvmClient client = new CvmClient(cred, "");

            String params = "{}";
            DescribeRegionsRequest describeRegionsRequest = DescribeRegionsRequest.fromJsonString(params, DescribeRegionsRequest.class);

            DescribeRegionsResponse resp = client.DescribeRegions(describeRegionsRequest);

            RegionResponse res = new RegionResponse();
            res.setDetails(Arrays.stream(resp.getRegionSet()).map(o -> new RegionResponse.RegionDetail(o.getRegion(), o.getRegionName())).collect(Collectors.toList()));
            return res;
        } catch (TencentCloudSDKException e) {
            return ECSResponse.buildByTencentCloudSDKException(e);
        }
    }
}
