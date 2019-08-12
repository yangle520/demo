package com.yangle.demo.model;

import com.alibaba.fastjson.JSONObject;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
public class KongService {

    private String     host;
    private Long       created_at;
    private Long       connect_timeout;
    private String     id;
    private String     protocol;
    private String     name;
    private Long       read_timeout;
    private Long       port;
    private String     path;
    private Long       updated_at;
    private Long       retries;
    private Long       write_timeout;
    private JSONObject extras;

}
