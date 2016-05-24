package com.will.imlearntest.controller;

import com.will.imlearntest.ListTools.PageModel;
import com.will.imlearntest.bo.UserBo;
import com.will.imlearntest.vo.UserStatusVo;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private UserBo userBo;

    @RequestMapping("index")
    public String index(HttpServletRequest request, HttpServletResponse response) {
        Object usernameObj = request.getSession().getAttribute("username");
        //System.err.println("Debug output:");
        //System.err.println(usernameObj);
        if(usernameObj != null) {
            PageModel<UserStatusVo> pageModel = userBo.listUser(1, 10);
            request.setAttribute("pageModel", pageModel);
        }
        return "master";
    }
}
