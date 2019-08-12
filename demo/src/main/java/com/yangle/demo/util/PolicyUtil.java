package com.yangle.demo.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.yangle.demo.model.PolicyDocument;
import com.yangle.demo.model.PolicyForKong;
import com.yangle.demo.model.StatementDocument;

import java.util.List;
import java.util.stream.Collectors;

public class PolicyUtil {

    public static void main(String[] args) {
        String p1 = "\n{\n  \"Statement\": [\n    {\n      \"Action\": \"oss:*\",\n      \"Effect\": \"Allow\",\n      \"Resource\": \"*\"\n    }\n  ],\n  \"Version\": \"1\"\n}\n                        ";
        String p2 = "{\n    \"Version\": \"1\", \n    \"Statement\": [\n        {\n            \"Action\": \"ecs:*\", \n            \"Resource\": \"*\", \n            \"Effect\": \"Allow\"\n        }, \n        {\n            \"Action\": [\n                \"vpc:DescribeVpcs\", \n                \"vpc:DescribeVSwitches\"\n            ], \n            \"Resource\": \"*\", \n            \"Effect\": \"Allow\"\n        }\n    ]\n}";

        List<String> policys = Lists.newArrayList(p1, p2);
        List<PolicyForKong> kongs = policys.stream()
                .flatMap(s -> {         // 获取 Statement 流
                    PolicyDocument pd = JSONObject.parseObject(s, PolicyDocument.class);
                    return pd.getStatement().stream();
                })
                .flatMap(s -> {         // 获取 action 流
                    List<PolicyForKong> pkList = Lists.newArrayList();
                    if (s.getAction() instanceof JSONArray) {

                    } else {

//                        if(){
//
//                        }
//                        String[] ac = String.valueOf(s.getAction()).split(":");
//                        PolicyForKong p = new PolicyForKong(s.getAction(), s.getAction(), s.getEffect());
                    }
                    return pkList.stream();
                }).collect(Collectors.toList());


        JSONObject json = JSONObject.parseObject(p2);
        JSONArray s = json.getJSONArray("Statement");
        Object a = s.getJSONObject(1).get("Action");
        System.out.println(a instanceof JSONArray);
        System.out.println(a);

    }
}
