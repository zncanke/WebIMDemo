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
    public @ResponseBody BaseResultVo login(@ModelAttribute("email") String email,
                       @ModelAttribute("password") String password,
                       HttpServletRequest request,
                       HttpServletResponse response) throws IOException {
        request.getSession().setAttribute("fromEmail", email);
        request.getSession().setAttribute("toEmail", "");
        if (userBo.login(email, password))
            return BaseResultVo.success;
        else
            return new BaseResultVo(0, "certification failed");
    }

    @RequestMapping("friendList")
    public void friendList(HttpServletRequest request, HttpServletResponse response)
                        throws IOException{
        List<UserStatusVo> res = userBo.listFriends((String)request.getSession().getAttribute("fromEmail"));
        request.getSession().setAttribute("friendList", res);
        /*for (UserStatusVo item : res) {
            System.out.println(item.getUsername() + " " + item.getStatus());
        }*/
        response.sendRedirect("/main");
    }

    @RequestMapping("detailinfo")
    public String detailinfo(@ModelAttribute("email") String email,
                             HttpServletRequest request, HttpServletResponse response) {
        request.getSession().setAttribute("detailEmail", email);
        PersonalInfoVo personalInfoVo = userBo.getPersonalInfo(email);
//        System.out.println(personalInfoVo.getEmail());
        request.getSession().setAttribute("personalInfoVo", personalInfoVo);
//        System.out.println(personalInfoVo);
        return "detailinfo";
    }

    @RequestMapping("saveDetailinfo")
    public String saveDetailinfo(@ModelAttribute("thename") String thename,
                                 @ModelAttribute("thesign") String thesign,
                                 @ModelAttribute("thegender") String thegender,
                                 HttpServletRequest request, HttpServletResponse response) {
        String detailEmail = (String)request.getSession().getAttribute("detailEmail");
        String fromEmail = (String)request.getSession().getAttribute("fromEmail");
        if (fromEmail.equals(detailEmail)) {
//            System.out.print(thename+" "+thesign);
            userBo.updatePersonalinfo(thename, thesign, thegender, fromEmail);
        }
        return "detailinfo";
    }
}
