package com.yangle.demo.openapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
public class RegionResponse extends CloudPageResponse {

    private List<RegionDetail> details;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RegionDetail {
        private String regionId;
        private String regionName;
    }
}
