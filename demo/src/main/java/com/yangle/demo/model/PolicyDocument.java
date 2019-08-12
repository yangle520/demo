package com.yangle.demo.model;

import lombok.Data;

import java.util.List;

@Data
public class PolicyDocument {

    private String Version;

    private List<StatementDocument> Statement;


}
