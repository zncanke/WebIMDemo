package com.will.imlearntest.controller;

import com.will.imlearntest.bo.ChatRecordBo;
import com.will.imlearntest.bo.UserBo;
import com.will.imlearntest.vo.ChatRecordVo;
import com.will.imlearntest.vo.UserStatusVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

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
        Map<String, UserStatusVo> list = (Map<String, UserStatusVo>)request.getSession().getAttribute("recentList");
        if (list.get(toEmail) == null) {
            UserStatusVo u = userBo.getUserByEmail(toEmail);
            u.setHaveUnread(false);
            list.put(toEmail, u);
            request.getSession().setAttribute("recentList", list);
        }
        String fromEmail = (String)request.getSession().getAttribute("fromEmail");
        request.getSession().setAttribute("toEmail", toEmail);
        request.getSession().setAttribute("fromUsername", userBo.getUsernameByEmail(fromEmail));
        request.getSession().setAttribute("toUsername", userBo.getUsernameByEmail(toEmail));
        List<ChatRecordVo> records = chatRecordBo.unreadBetween(toEmail, fromEmail);
        if (records.size() < 10) {
            List<ChatRecordVo> extra = chatRecordBo.recordBetween(fromEmail, toEmail);
            extra.addAll(records);
            records = extra;
        }
        request.getSession().setAttribute("records", records);
        return "/chatbox";
    }
}
