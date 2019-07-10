package com.yangle.demo.model;

import lombok.Data;

import java.util.List;

@Data
public class KongRoute {

    private Long         updated_at;
    private Long         created_at;
    private Boolean      strip_path;
    private String       snis;
    private List<String> hosts;
    private String       name;
    private List<String> methods;
    private String       sources;
    private Boolean       preserve_host;
    private Integer      regex_priority;
    private KongIdDto    service;
    private List<String> paths;
    private String       destinations;
    private String       id;
    private List<String> protocols;
    private String       tags;

}
