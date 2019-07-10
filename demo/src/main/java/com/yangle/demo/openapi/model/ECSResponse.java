package com.yangle.demo.openapi.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class ECSResponse extends CloudPageResponse {

    private List<ECSInfo> details;

    @Data
    @Builder(toBuilder = true)
    public static class ECSInfo {
        private String instanceName;

        private String publicIP;

        private String innerIP;

        private String desktop;

        private String expireTime;
    }
}
