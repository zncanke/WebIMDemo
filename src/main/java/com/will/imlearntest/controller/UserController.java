package com.will.imlearntest.controller;

import com.will.imlearntest.ListTools.PageModel;
import com.will.imlearntest.bo.UserBo;
import com.will.imlearntest.po.FriendshipPo;
import com.will.imlearntest.vo.BaseResultVo;
import com.will.imlearntest.vo.UserStatusVo;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by willl on 5/18/16.
 */
@Controller
@RequestMapping("user")
public class UserController {
    @Autowired
    private UserBo userBo;

    @RequestMapping("login")
    public @ResponseBody BaseResultVo login(@ModelAttribute("username") String username,
                       @ModelAttribute("password") String password,
                       HttpServletRequest request,
                       HttpServletResponse response) throws IOException {
        request.getSession().setAttribute("fromUserName", username);
        request.getSession().setAttribute("toUserName", "");
//        System.out.println(username+" "+password);
        if (userBo.login(username, password))
            return BaseResultVo.success;
        else
            return new BaseResultVo(0, "certification failed");
    }

   /* @RequestMapping("userList")
    public String userList(@ModelAttribute("page") int page, @ModelAttribute("limit") int limit,
                           HttpServletRequest request, HttpServletResponse response) {
        if (page <= 0) {
            page = 1;
        }

        PageModel<UserStatusVo> pageModel = userBo.listUser(page , limit);

        request.setAttribute("pageModel", pageModel);
        return "/user/userList";
    }*/
    @RequestMapping("friendList")
    public void friendList(HttpServletRequest request, HttpServletResponse response)
                        throws IOException{
        List<UserStatusVo> res = userBo.listFriends((String)request.getSession().getAttribute("fromUserName"));
        request.getSession().setAttribute("friendList", res);
        /*for (UserStatusVo item : res) {
            System.out.println(item.getUsername() + " " + item.getStatus());
        }*/
        response.sendRedirect("/main");
    }

}
