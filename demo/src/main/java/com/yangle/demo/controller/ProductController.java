package com.yangle.demo.controller;

import com.yangle.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    /**
     * 生成一条instanceId
     *
     * @param namespace
     * @param cloudName
     * @return
     */
    @RequestMapping("/test/produce/instance-id")
    public String getInstanceId(String namespace, String cloudName) {
        return productService.getInstanceId(namespace, cloudName);
    }

    /**
     * 开始持续生成instanceId
     *
     * @return
     */
    @RequestMapping("/test/produce/start")
    public String startCreateInstanceId() {
        productService.start();
        return "success";
    }

    /**
     * 停止生成instanceId
     *
     * @return
     */
    @RequestMapping("/test/produce/stop")
    public String stopCreateInstanceId() {
        productService.stop();
        return "success";
    }

}
