package com.yangle.demo.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.odiszapc.nginxparser.NgxBlock;
import com.github.odiszapc.nginxparser.NgxConfig;
import com.github.odiszapc.nginxparser.NgxEntry;
import com.google.common.collect.Lists;
import com.yangle.demo.model.KongIdDto;
import com.yangle.demo.model.KongRoute;
import com.yangle.demo.model.KongService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * nginx,conf - service 转成 kong 配置快照
 */
@RestController
@RequestMapping("/kong")
public class KongConfController {

    @RequestMapping(value = "/trans", produces = "application/json")
    public String trans(MultipartFile file) throws IOException {

        // 先正则替换调变量
        String fileStr = new String(file.getBytes()).replaceAll("\\$\\{[A-Z_]*\\}", "xxx");

        // 解析成ngxConf
        NgxConfig conf = NgxConfig.read(new ByteArrayInputStream(fileStr.getBytes("UTF-8")));
        // 获取 server 模块
        List<NgxEntry> servers = conf.findAll(NgxConfig.BLOCK, "http", "server");

        // conf 解析结果
        List<String> apis = Lists.newArrayList();
        for (NgxEntry s : servers) {
            String listen = ((NgxBlock) s).findParam("listen").getValue();

            if (listen.startsWith("12011")) { // 控制台端口 12011
                List<NgxEntry> locations = ((NgxBlock) s).findAll(NgxConfig.BLOCK, "location");

                for (NgxEntry l : locations) {
                    String path = ((NgxBlock) l).getValue();

                    if (path.startsWith("/api/")) {
                        String proxyPass = ((NgxBlock) l).findParam("proxy_pass").getValue();
                        System.out.println(path + "##" + proxyPass);
                        apis.add(path + "##" + proxyPass);
                    }
                }
            }
        }

        // 生成快照json
        JSONObject json = new JSONObject();
        json.put("createdUser", null);
        json.put("updatedUser", null);
        json.put("id", 1);
        json.put("name", file.getName());
        json.put("kong_node_name", "http://10.253.146.17");
        json.put("kong_node_url", "http://kong-admin:8001");
        json.put("kong_version", "1.1.2");
        json.put("createdAt", "2019-08-12T06:59:21.000Z");
        json.put("updatedAt", "2019-08-12T06:59:21.000Z");

        JSONArray services = new JSONArray();
        JSONArray routes = new JSONArray();

        apis.forEach(api -> {
            String path = api.substring(0, api.indexOf("##"));
            String host = api.substring(api.indexOf("http://") + 7, api.lastIndexOf(":"));
            String port = api.substring(api.lastIndexOf(":") + 1).replace("/", "");
            String id = UUID.randomUUID().toString();

            KongService s = KongService.builder()
                    .host(host)
                    .id(id)
                    .name(path.replace("/", ""))
                    .port(Long.valueOf(port))
                    .protocol("http")
                    .path(api.substring(api.indexOf(port) + port.length()))
                    .retries(5L)
                    .connect_timeout(60000L)
                    .read_timeout(60000L)
                    .write_timeout(60000L)
                    .created_at(System.currentTimeMillis())
                    .updated_at(System.currentTimeMillis())
                    .build();
            KongRoute r = KongRoute.builder()
                    .id(UUID.randomUUID().toString())
                    .name(path.replace("/", ""))
                    .paths(Lists.newArrayList(path))
                    .service(new KongIdDto(id))
                    .regex_priority(2)
                    .strip_path(true)
                    .created_at(System.currentTimeMillis())
                    .updated_at(System.currentTimeMillis())
                    .build();

            services.add(s);
            routes.add(r);
        });


        JSONObject data = new JSONObject();
        data.put("services", services);
        data.put("routes", routes);
        json.put("data", data);
        return json.toString();
    }

}
