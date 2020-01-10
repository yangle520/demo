package com.yangle.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.yangle.demo.util.SignatureUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
public class AKController {

    @RequestMapping("/ak/uri")
    public String ak(HttpServletRequest request, String ak, String aks, String method) throws Exception {
        Map<String, String[]> map = request.getParameterMap();

        JSONObject json = new JSONObject();
        for (String key : map.keySet()) {
            if (!key.equals("ak") && !key.equals("aks")) {
                json.put(key, map.get(key)[0]);
            }
        }

        return SignatureUtil.getUrl(json, ak, aks);
    }
}
