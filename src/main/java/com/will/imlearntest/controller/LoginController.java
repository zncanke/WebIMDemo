package com.will.imlearntest.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
@RequestMapping("login")
public class LoginController {
    @RequestMapping("")
    public String index(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        if (request.getSession().getAttribute("username") == null) {
            return "/login";
        } else {
            response.sendRedirect("/main");
            return null;
        }
    }

}
