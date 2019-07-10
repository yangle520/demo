package com.yangle.demo.model;

import lombok.Data;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * 接口响应
 *
 * @param <T>
 * @author 林少强
 * @date 2018-11-13
 */
@Data
public class RestfulEntity<T> {

    /**
     * 接口调用返回状态，TRUE为正常返回，FALSE为异常返回
     */
    private Boolean status;
    /**
     * 用户认证状态，TRUE为已认证，FALSE为未认证
     */
    private Boolean auth;
    /**
     * 接口调用返回编码，暂未使用，默认为0(正常)，
     * 当调用出错时,默认为1(异常)，可自行填写对应错误码
     */
    private String code;
    /**
     * 接口调用返回数据
     */
    private T res;
    /**
     * 调用成功或者失败都可以设置消息
     */
    private String msg;

    public RestfulEntity(Boolean status, Boolean auth, String code, T res) {
        this.status = status;
        this.auth = auth;
        this.code = code;
        this.res = res;
    }

    public RestfulEntity(Boolean status, Boolean auth, String code, T res, String msg) {
        this.status = status;
        this.auth = auth;
        this.code = code;
        this.res = res;
        this.msg = msg;
    }


    /**
     * 返回异常请求时调用，须重新登录
     *
     * @param res
     * @return
     */
    public static <T> RestfulEntity<T> getAuthFailure(T res) {
        return new RestfulEntity<>(false, false, "1", res);
    }

    /**
     * 返回异常请求时调用，无须重新登录
     *
     * @param res
     * @return
     */
    public static <T> RestfulEntity<T> getFailure(T res) {
        return new RestfulEntity<>(false, true, "1", res);
    }

    /**
     * 返回异常请求时调用，无须重新登录
     * 自定义失败消息
     *
     * @param res
     * @param msg
     * @param <T>
     * @return
     */
    public static <T> RestfulEntity<T> getFailure(T res, String msg) {
        return new RestfulEntity<>(false, true, "1", res, msg);
    }




    /**
     * 返回正常请求时调用
     *
     * @param res
     * @return
     */
    public static <T> RestfulEntity<T> getSuccess(T res) {
        Object isAuth = RequestContextHolder.getRequestAttributes().getAttribute("is_auth", RequestAttributes.SCOPE_REQUEST);
        boolean auth = true;
        if(isAuth == null || !((Boolean) isAuth)) {
            auth = false;
        }
        return new RestfulEntity<>(true, auth, "0", res);
    }

    /**
     * 返回正常请求时调用
     * 自定义成功消息
     *
     * @param res
     * @param <T>
     * @return
     */
    public static <T> RestfulEntity<T> getSuccess(T res, String msg) {
        Object isAuth = RequestContextHolder.getRequestAttributes().getAttribute("is_auth", RequestAttributes.SCOPE_REQUEST);
        boolean auth = true;
        if(isAuth == null || !((Boolean) isAuth)) {
            auth = false;
        }
        return new RestfulEntity<>(true, auth, "0", res, msg);
    }

}

