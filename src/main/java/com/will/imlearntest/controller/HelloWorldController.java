package com.will.imlearntest.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.will.imlearntest.bo.HelloWorldBo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("hi")
public class HelloWorldController {
    @Autowired
    private HelloWorldBo helloWorldBo;
    @RequestMapping("helloworld")
    public String index(HttpServletRequest request, HttpServletResponse response) {
        String hello = helloWorldBo.getHello();
        request.setAttribute("helloworld", hello);
        return "helloworld";
    }
}