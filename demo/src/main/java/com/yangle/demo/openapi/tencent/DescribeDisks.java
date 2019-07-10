package com.yangle.demo.openapi.tencent;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;

import com.tencentcloudapi.cbs.v20170312.CbsClient;

import com.tencentcloudapi.cbs.v20170312.models.DescribeDisksRequest;
import com.tencentcloudapi.cbs.v20170312.models.DescribeDisksResponse;

public class DescribeDisks {

    public static void main(String [] args) {
        try{

//            Credential cred = new Credential("AKIDkKgomWtIKBnBMJ9VklX4NRL1SfeOqBD0", "g8LK5bsyqTr0vb3HH2YqwoStBF6i5E8n");
            Credential cred = new Credential("AKIDGH2XrTgyvc5IGyl1f5IL8fxNYT8YAAeO", "d3D7mUxA7oGUeZAVhdMq54KQHsEKCqHW");

            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("cbs.tencentcloudapi.com");

            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);

            CbsClient client = new CbsClient(cred, "ap-beijing", clientProfile);

            String params = "{}";
            DescribeDisksRequest req = DescribeDisksRequest.fromJsonString(params, DescribeDisksRequest.class);

            DescribeDisksResponse resp = client.DescribeDisks(req);

            System.out.println(DescribeDisksRequest.toJsonString(resp));
        } catch (TencentCloudSDKException e) {
            System.out.println(e.toString());
        }

    }
}
