package com.jjh.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

public class HelloController {
    //git,hello!
    @RequestMapping("/hello")
    @ResponseBody
    public String hello() {
        return "hello, shiro!";
    }

    @RequestMapping("/test")
    @ResponseBody
    public void say() {
        System.out.println("this is for you, shiro!");
    }
}
