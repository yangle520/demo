package com.yangle.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.yangle.demo.util.certification.TencentCertification;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class AIController {

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String init() {
        return "index";
    }

    @RequestMapping(value = "/res", method = RequestMethod.GET)
    public String res() {
        return "res";
    }

    @RequestMapping(value = "/uploadInfo", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject uploadInfo(@RequestParam String name,
                                 @RequestParam String idNo) {
        JSONObject json = new JSONObject();
        // 本次请求订单号
        String orderNo = buildOrderNo();

        String userId = "userId" + String.format("%020d", System.currentTimeMillis());
        String res = TencentCertification.getH5faceId(orderNo, userId, name, idNo);

        JSONObject j = JSONObject.parseObject(res);
        if ("0".equals(j.getString("code"))) {
            String faceId = j.getJSONObject("result").getString("h5faceId");
            json.put("code", 0);
            json.put("url", TencentCertification.init(buildOrderNo(), userId, faceId));
        } else {
            json.put("code", j.getString("code"));
            json.put("msg", j.getString("msg"));
        }
        System.out.println("uploadInfo:" + json.toJSONString());
        return json;
    }

    @RequestMapping(value = "/queryRes", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject queryRes(@RequestParam String orderNo) {
        return TencentCertification.queryIdascRes(orderNo);
    }

    @RequestMapping(value = "/uploadPerson", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject uploadIdCardPic0(@RequestParam("person") MultipartFile person) {
        return TencentCertification.getIdCareInfo("0", person);
    }

    @RequestMapping(value = "/uploadEmblem", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject uploadIdCardPic1(@RequestParam("file") MultipartFile file) {
        return TencentCertification.getIdCareInfo("1", file);
    }

    private String buildOrderNo() {
        return "orderNo" + String.format("%020d", System.currentTimeMillis());
    }
}
