package com.yangle.demo.openapi;

import lombok.Data;

@Data
public abstract class OpenApiRequest {

    private String regionId    = null;
    private String accessKeyId = null;

    private String accessSecret = null;
}
