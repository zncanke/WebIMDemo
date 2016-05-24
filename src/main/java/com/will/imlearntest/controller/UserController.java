package com.will.imlearntest.controller;

import com.will.imlearntest.ListTools.PageModel;
import com.will.imlearntest.bo.UserBo;
import com.will.imlearntest.vo.UserStatusVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by willl on 5/18/16.
 */
@Controller
@RequestMapping("user")
public class UserController {
    @Autowired
    private UserBo userBo;

    @RequestMapping("login")
    public void login(@ModelAttribute("username") String username, HttpServletRequest request,
                      HttpServletResponse response) throws IOException {
        request.getSession().setAttribute("username", username);
        userBo.login(username);
        response.sendRedirect("/master/index");
    }

    @RequestMapping("userList")
    public String userList(@ModelAttribute("page") int page, @ModelAttribute("limit") int limit,
                           HttpServletRequest request, HttpServletResponse response) {
        if (page <= 0) {
            page = 1;
        }

        PageModel<UserStatusVo> pageModel = userBo.listUser(page , limit);

        request.setAttribute("pageModel", pageModel);
        return "/user/userList";
    }
}
