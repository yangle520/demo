package com.yangle.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Work {
    private Long   workId;
    private String arrangeIn1;
    private String arrangeOut1;
    private String arrangeIn2;
    private String arrangeOut2;
}
