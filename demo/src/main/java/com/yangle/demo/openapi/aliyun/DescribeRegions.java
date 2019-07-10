package com.yangle.demo.openapi.aliyun;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.profile.DefaultProfile;
import com.google.gson.Gson;
import com.aliyuncs.ecs.model.v20140526.*;

public class DescribeRegions {

    public static void main(String[] args) {
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", "LTAIxga6OlW0XHMp", "meLOTsA1I6jOb54Iru1eNXNVMdvNKU");

        IAcsClient client = new DefaultAcsClient(profile);

        DescribeRegionsRequest request = new DescribeRegionsRequest();

        try {
            DescribeRegionsResponse response = client.getAcsResponse(request);
            System.out.println(new Gson().toJson(response));
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            System.out.println("ErrCode:" + e.getErrCode());
            System.out.println("ErrMsg:" + e.getErrMsg());
            System.out.println("RequestId:" + e.getRequestId());
        }

    }

}
