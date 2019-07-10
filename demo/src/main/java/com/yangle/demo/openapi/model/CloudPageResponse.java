package com.yangle.demo.openapi.model;

import com.yangle.demo.openapi.OpenApiResponse;
import lombok.Data;

@Data
public class CloudPageResponse extends OpenApiResponse {

    private Integer totalCount = 0;

    private Integer pageNumber = 0;

    private Integer pageSize = 0;
}
