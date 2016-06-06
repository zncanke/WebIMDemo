package com.will.imlearntest.controller;

import com.will.imlearntest.bo.ChatRecordBo;
import com.will.imlearntest.bo.UserBo;
import com.will.imlearntest.vo.ChatRecordVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("chat")
public class ChatController {

    @Autowired
    private ChatRecordBo chatRecordBo;
    @Autowired
    private UserBo userBo;

    @RequestMapping("chatbox")
    public String chatbox( @ModelAttribute("toEmail") String toEmail,
                           HttpServletRequest request,
                           HttpServletResponse response) {
//        System.out.println(toEmail);
        String fromEmail = (String)request.getSession().getAttribute("fromEmail");
        request.getSession().setAttribute("toEmail", toEmail);
        request.getSession().setAttribute("fromUsername", userBo.getUsernameByEmail(fromEmail));
        System.out.println((String)request.getSession().getAttribute("fromUsername"));
        request.getSession().setAttribute("toUsername", userBo.getUsernameByEmail(toEmail));
        List<ChatRecordVo> records = chatRecordBo.recordBetween(fromEmail, toEmail);
        request.getSession().setAttribute("records", records);
        return "/chatbox";
    }

}
