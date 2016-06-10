package com.will.imlearntest.controller;

import com.will.imlearntest.bo.UserBo;
import com.will.imlearntest.vo.UserStatusVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by willl on 6/3/16.
 */

@Controller
@RequestMapping("friend")
public class FriendsController {
    @Autowired
    UserBo userBo;

    @RequestMapping("searchInterface")
    public String searchInterface(HttpServletRequest request, HttpServletResponse response) {
        return "searchInterface";
    }
    @RequestMapping("searchUser")
    public String searchUser(@ModelAttribute("userCondition") String userCondition,
                             HttpServletRequest request, HttpServletResponse response) {
        String fromEmail = (String) request.getSession().getAttribute("fromEmail");
        List<UserStatusVo> list = userBo.searchUser(userCondition, fromEmail);
        request.getSession().setAttribute("searchUserResult", list);
        return "searchUserResult";
    }
}
