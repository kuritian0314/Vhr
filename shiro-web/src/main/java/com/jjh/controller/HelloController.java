package com.jjh.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

public class HelloController {
    @RequestMapping("/hello")
    @ResponseBody
    public String hello() {
        return "hello, shiro!";
    }
}
