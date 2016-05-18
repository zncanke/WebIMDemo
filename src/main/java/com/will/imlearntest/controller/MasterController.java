package com.will.imlearntest.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by willl on 5/18/16.
 */

@Controller
@RequestMapping("master")
public class MasterController {
    @RequestMapping("index")
    public String index(HttpServletRequest request, HttpServletResponse response) {
        return "master";
    }
}
