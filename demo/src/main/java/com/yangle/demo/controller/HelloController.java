package com.yangle.demo.controller;

import com.yangle.demo.model.RestfulEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/uco")
public class HelloController {


    @GetMapping(params = "Action=CreateDBInstance")
    public void CreateDBInstance(@RequestHeader(name = "x-user-id", required = false) String userId,
                                 @RequestHeader(name = "RequestId", required = false) String requestId) {
        log.info("userId:{},requestId:{}", userId, requestId);
//        return "";
    }

    @GetMapping(params = "Action=DeleteDBInstance")
    public RestfulEntity<Long> hello(@RequestHeader(name = "x-user-id", required = false) String userId,
                                     @RequestHeader(name = "RequestId", required = false) String requestId,
                                     @RequestParam(name = "name", required = false) String name,
                                     @RequestParam(name = "id", required = false) String id) {

//        try {
////            Thread.sleep(10000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        log.info("userId:{},requestId:{}", userId, requestId);
        return new RestfulEntity(true, true, userId, 12345678912345l, "abcd");
    }


    @RequestMapping("/a/b/b")
    public String abb() {
        return "abb";
    }

    @RequestMapping("/a/a")
    public String aa() {
        return "aa";
    }


}
