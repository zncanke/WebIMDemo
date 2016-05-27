package com.will.imlearntest.controller;

import com.will.imlearntest.ListTools.PageModel;
import com.will.imlearntest.bo.UserBo;
import com.will.imlearntest.vo.UserStatusVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by willl on 5/18/16.
 */

@Controller
@RequestMapping("master")
public class MasterController {
    @Autowired
    private UserBo userBo;

    @RequestMapping("index")
    public String index(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        Object usernameObj = request.getSession().getAttribute("username");
        if(usernameObj != null) {
//            PageModel<UserStatusVo> pageModel = userBo.listUser(1, 10);
//            request.setAttribute("pageModel", pageModel);
        } else {
            response.sendRedirect("/login");
            return null;
        }
        return "master";
    }
}
