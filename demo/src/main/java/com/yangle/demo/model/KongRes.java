package com.yangle.demo.model;

import lombok.Data;

import java.util.List;

@Data
public class KongRes {
    private String next;
    private List<KongPluginDto> data;
}
