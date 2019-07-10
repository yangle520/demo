package com.yangle.demo.model;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.util.List;

@Data
public class KongPlugin {

    private Long         created_at;
    private JSONObject   config;
    private String       id;
    private KongIdDto    service;
    private String       name;
    private List<String> protocols;
    private Boolean      enabled;
    private String       run_on;
    private KongIdDto    consumer;
    private KongIdDto    route;
    private String       tags;

}
