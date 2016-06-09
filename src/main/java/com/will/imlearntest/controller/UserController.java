package com.will.imlearntest.controller;

import com.will.imlearntest.bo.ChatRecordBo;
import com.will.imlearntest.bo.UserBo;
import com.will.imlearntest.po.FriendshipPo;
import com.will.imlearntest.vo.BaseResultVo;
import com.will.imlearntest.vo.PersonalInfoVo;
import com.will.imlearntest.vo.UserStatusVo;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.registry.infomodel.User;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by willl on 5/18/16.
 */
@Controller
@RequestMapping("user")
public class UserController {
    @Autowired
    private UserBo userBo;


    @RequestMapping("login")
    public @ResponseBody BaseResultVo login(@ModelAttribute("email") String email,
                       @ModelAttribute("password") String password,
                       HttpServletRequest request,
                       HttpServletResponse response) throws IOException {
        String oEmail = (String)request.getSession().getAttribute("fromEmail");
        request.getSession().setAttribute("fromEmail", email);
        request.getSession().setAttribute("toEmail", "");
        request.getSession().setAttribute("listStatus", "recent");
        request.getSession().setAttribute("boxStatus", "nothing");
        if (userBo.login(email, password)) {
            if (oEmail != null)
                userBo.userLogout(oEmail);
            refreshRecent(request);
            return BaseResultVo.success;
        } else
            return new BaseResultVo(0, "certification failed");
    }

    @RequestMapping("friendList")
    public String friendList(HttpServletRequest request, HttpServletResponse response)
                        throws IOException{
        request.getSession().setAttribute("listStatus", "friendList");
        Map<String, Map<String, UserStatusVo>> res = userBo.listFriends((String)request.getSession().getAttribute("fromEmail"));
        request.getSession().setAttribute("friendList", res);
        /*for (UserStatusVo item : res) {
            System.out.println(item.getUsername() + " " + item.getStatus());
        }*/
        return "friendlist";
    }

    @RequestMapping("detailinfo")
    public String detailinfo(@ModelAttribute("email") String email,
                             HttpServletRequest request, HttpServletResponse response) {
        if (email.equals("self"))
            email = (String)request.getSession().getAttribute("fromEmail");
        request.getSession().setAttribute("boxStatus", "detail:" + email);
        request.getSession().setAttribute("detailEmail", email);
        PersonalInfoVo personalInfoVo = userBo.getPersonalInfo(email);
//        System.out.println(personalInfoVo.getEmail());
        request.getSession().setAttribute("personalInfoVo", personalInfoVo);
//        System.out.println(personalInfoVo);
        return "detailinfo";
    }

    @RequestMapping("addGroup")
    @ResponseBody
    public BaseResultVo addGroup(@ModelAttribute("newGroup") String newGroup,
                         HttpServletRequest request, HttpServletResponse response) {
        String email = (String)request.getSession().getAttribute("fromEmail");
        int res = userBo.addGroup(email, newGroup);
        if (res > 0)
            return BaseResultVo.success;
        else
            return new BaseResultVo(0, "There maybe have the same group.");
    }

    @RequestMapping("recent")
    public String recent(HttpServletRequest request, HttpServletResponse response) {
        request.getSession().setAttribute("listStatus", "recent");
        refreshRecent(request);
        return "recent";
    }

    private void refreshRecent(HttpServletRequest request) {

        Map<String, UserStatusVo> list = (Map<String, UserStatusVo>)request.getSession().getAttribute("recentList");
        if (list == null) {
            list = new HashMap<String, UserStatusVo>();
            list.clear();
        }
        list = userBo.recentList((String)request.getSession().getAttribute("fromEmail"), list);
        request.getSession().setAttribute("recentList", list);
    }
}
