package com.yangle.demo.controller;

import com.yangle.demo.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/iam")
@Slf4j
public class IAMController {

    @PostMapping(params = "Action=user")
    public User post(@RequestBody User u) {
        log.info("11111111111");
        return u;
    }

    @PostMapping("/aaa")
    public User post2(@RequestBody User u) {
        log.info("aaaaaaa");
        return u;
    }

    @PostMapping("/bbb")
    public User post3(@RequestBody User u) {
        log.info("bbbbbbbbb");
        return u;
    }
}
