package com.yangle.demo.controller;


import com.alibaba.fastjson.JSONObject;
import com.yangle.demo.model.KongPlugin;
import com.yangle.demo.model.KongRoute;
import com.yangle.demo.model.KongService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
public class KongController {

//    @Autowired
//    private KongService kongService;
//
//    @RequestMapping("/test/change/kong/plugins/status")
//    public Boolean changeZServiceStatus(Integer status) {
//        return kongService.changeZServiceStatus(status);
//    }

    @RequestMapping("/filter/service")
    public String jsonFilter(MultipartFile file, String serviceName) throws IOException {

        JSONObject json = JSONObject.parseObject(new String(file.getBytes()));

        List<KongService> services = json.getJSONObject("data").getJSONArray("services").toJavaList(KongService.class);

        List<KongRoute> routes = json.getJSONObject("data").getJSONArray("routes").toJavaList(KongRoute.class);

        List<KongPlugin> plugins = json.getJSONObject("data").getJSONArray("plugins").toJavaList(KongPlugin.class);

        List<String> names = Arrays.stream(serviceName.split(",")).collect(Collectors.toList());

        // 匹配名字相同的service
        services = services.stream().filter(s -> names.contains(s.getName())).collect(Collectors.toList());
        // 获取serviceId
        List<String> serviceIds = services.stream().map(KongService::getId).collect(Collectors.toList());
        // 根据serviceId 匹配 route
        routes = routes.stream().filter(r -> serviceIds.contains(r.getService().getId())).collect(Collectors.toList());
        //根据serviceId 匹配 plugins
        plugins = plugins.stream().filter(p -> serviceIds.contains(p.getService().getId())).collect(Collectors.toList());

        json.getJSONObject("data").put("services", services);
        json.getJSONObject("data").put("routes", routes);
        json.getJSONObject("data").put("plugins", plugins);

        return json.toJSONString();
    }

}
