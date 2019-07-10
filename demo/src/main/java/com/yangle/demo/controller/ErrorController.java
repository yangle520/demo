package com.yangle.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
public class ErrorController {

    @RequestMapping("/failed")
    public String failed(HttpServletResponse response) {
        response.setStatus(404);
        return "error";
    }
}
