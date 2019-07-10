package com.yangle.demo.openapi.model;

import com.yangle.demo.openapi.OpenApiRequest;
import lombok.Data;

@Data
public class ECSRequest extends OpenApiRequest {

    private String instanceId;

    private String instanceName;

}
