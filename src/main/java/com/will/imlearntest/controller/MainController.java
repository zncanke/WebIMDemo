package com.will.imlearntest.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("main")
public class MainController {
    @RequestMapping("")
    public String index(HttpServletRequest request, HttpServletResponse response) {
        Object usernameObj = request.getSession().getAttribute("fromUserName");
        if(usernameObj != null) {
            return "/main";
        } else {
            return "/login";
        }
    }
}
