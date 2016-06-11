package com.will.imlearntest.controller;

import com.will.imlearntest.bo.UserBo;
import com.will.imlearntest.vo.UserStatusVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        try {
            userCondition = URLDecoder.decode(userCondition, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        String fromEmail = (String) request.getSession().getAttribute("fromEmail");
        List<UserStatusVo> list = userBo.searchUser(userCondition, fromEmail);
        request.getSession().setAttribute("searchUserResult", list);
        return "searchUserResult";
    }
    @RequestMapping("searchFriend")
    public String searchFriend(@ModelAttribute("condition") String condition,
                               HttpServletRequest request, HttpServletResponse response) {
        try {
            condition = URLDecoder.decode(condition, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        request.getSession().setAttribute("listStatus", "recent");
        Map<String, UserStatusVo> ret = new HashMap<String, UserStatusVo>();
        ret.clear();
        Map<String, Map<String, UserStatusVo>> list = (Map<String, Map<String, UserStatusVo>>) request.getSession().getAttribute("friendList");
        for (String g : list.keySet()) {
            Map<String, UserStatusVo> group = list.get(g);
            for (String s : group.keySet()) {
                UserStatusVo u = group.get(s);
                if (u.getEmail().contains(condition) || u.getUsername().contains(condition)) {
                    ret.put(s, u);
                }
            }
        }
        request.getSession().setAttribute("searchList", ret);
        return "searchFriend";
    }
}
