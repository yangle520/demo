package com.yangle.demo.model;

import lombok.Data;

@Data
public class ResultVO<T> {
    private Integer code;

    private String msg;

    private T res;

    public ResultVO() {
    }

    public ResultVO(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResultVO(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.res = data;
    }

}
