package com.yangle.demo.controller;

import com.yangle.demo.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/iam")
@Slf4j
public class IAMController {

    @PostMapping(params = "Action=user")
    public String post(@RequestHeader(value = "iam-user-id", required = false) String userId,
                       @RequestHeader(value = "iam-user-name", required = false) String userName,
                       @RequestHeader(value = "iam-user-type", required = false) String userType,
                       @RequestHeader(value = "iam-parent-id", required = false) String parentId,
                       @RequestHeader(value = "iam-parent-name", required = false) String parentName) {
        log.info("{}$${}$${}$${}$${}", userId, userName, userType, parentId, parentName);
        return String.format("%s|||%s|||%s|||%s|||%s", userId, userName, userType, parentId, parentName);
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
