package com.yangle.demo.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class WorkDetail {

    private Long    userId;
    private String  userName;
    private String  workDate;
    private Integer lateTimes;
    private Integer forgetTimes;
}
