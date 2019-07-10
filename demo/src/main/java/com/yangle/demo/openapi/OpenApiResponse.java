package com.yangle.demo.openapi;

import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Slf4j
public class OpenApiResponse {

    private String requestId;

    private String errCode;

    private String errMsg;

    public static OpenApiResponse buildByTencentCloudSDKException(TencentCloudSDKException e) {
        log.error("TencentCloudSDKException:", e);
        OpenApiResponse res = new OpenApiResponse();
        if (e.getMessage().contains("-")) {
            res.setErrMsg(e.getMessage().substring(e.getMessage().indexOf("-") + 1));
            res.setErrCode(e.getMessage().substring(0, e.getMessage().indexOf("-")));
        } else {
            res.setErrMsg(e.getMessage());
        }
        res.setRequestId(e.getRequestId());
        return res;
    }

    public static OpenApiResponse buildByAliClientException(ClientException e) {
        log.error("AliClientException:", e);
        OpenApiResponse res = new OpenApiResponse();
        res.setRequestId(e.getRequestId());
        res.setErrMsg(e.getErrMsg());
        res.setErrCode(e.getErrCode());
        return res;
    }

    public static OpenApiResponse buildByAliServerException(ServerException e) {
        log.error("AliServerException:", e);
        OpenApiResponse res = new OpenApiResponse();
        res.setErrMsg("请求发送失败！");
        res.setErrCode("REQUEST SEND ERROR");
        return res;
    }

    public static OpenApiResponse buildByClassNotFoundException(ClassNotFoundException e) {
        log.error("ClassNotFoundException:", e);
        OpenApiResponse res = new OpenApiResponse();
        res.setErrMsg("当前方法暂未提供");
        res.setErrCode("CLASS NOT FOUND");
        return res;
    }

    public static OpenApiResponse buildByException(Exception e) {
        log.error("Exception:", e);
        OpenApiResponse res = new OpenApiResponse();
        res.setErrMsg("发生未知错误！");
        res.setErrCode("ERROR");
        return res;
    }
}
