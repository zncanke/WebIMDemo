package com.will.imlearntest.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by willl on 6/3/16.
 */

@Controller
@RequestMapping("friend")
public class FriendsController {
    @RequestMapping("searchInterface")
    public String searchInterface() {
        return "searchInterface";
    }
}
