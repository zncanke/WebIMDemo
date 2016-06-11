package com.will.imlearntest.controller;

import com.sun.tracing.dtrace.ModuleAttributes;
import com.will.imlearntest.bo.ChatRecordBo;
import com.will.imlearntest.bo.UserBo;
import com.will.imlearntest.vo.ChatRecordVo;
import com.will.imlearntest.vo.PersonalInfoVo;
import com.will.imlearntest.vo.UserStatusVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
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
        request.getSession().setAttribute("boxStatus", toEmail);
        if (toEmail.equals("systemInfo@sys.com")) {
            String email = (String)request.getSession().getAttribute("fromEmail");
            Map<String, UserStatusVo> applyList = chatRecordBo.applyList(email);
            request.getSession().setAttribute("applyList", applyList);
            return "sysinfo";
        }
        Map<String, UserStatusVo> list = (Map<String, UserStatusVo>)request.getSession().getAttribute("recentList");
        if (list.get(toEmail) == null) {
            UserStatusVo u = userBo.getUserByEmail(toEmail);
            u.setHaveUnread(false);
            list.put(toEmail, u);
            request.getSession().setAttribute("recentList", list);
        } else {
            list.get(toEmail).setHaveUnread(false);
        }
        String fromEmail = (String)request.getSession().getAttribute("fromEmail");
        PersonalInfoVo p = userBo.getPersonalInfo(toEmail);
        request.getSession().setAttribute("personalInfoVo", p);
        request.getSession().setAttribute("toEmail", toEmail);
        request.getSession().setAttribute("fromUsername", userBo.getUsernameByEmail(fromEmail));
        request.getSession().setAttribute("toUsername", userBo.getUsernameByEmail(toEmail));
        List<ChatRecordVo> records = chatRecordBo.unreadBetween(toEmail, fromEmail);
        List<ChatRecordVo> res = new ArrayList<ChatRecordVo>();
        res.clear();
        if (records.size() < 10) {
            List<ChatRecordVo> extra = chatRecordBo.recordBetween(fromEmail, toEmail);
            for (ChatRecordVo c : extra) {
               if (records.size() > 0 && c.equal(records.get(0)))
                   break;
                else
                   res.add(c);
            }
            for (ChatRecordVo c : records)
                res.add(c);
        } else
            res = records;
        request.getSession().setAttribute("records", res);
        return "/chatbox";
    }

    @RequestMapping("removeApply")
    public void removeApply(@ModelAttribute("email") String email,
                            HttpServletRequest request,
                            HttpServletResponse response) {
        Map<String, UserStatusVo> list = (Map<String, UserStatusVo>)request.getSession().getAttribute("applyList");

        list.remove(email);
        if (list.isEmpty()) {
            Map<String, UserStatusVo> l = (Map<String, UserStatusVo>)request.getSession().getAttribute("recentList");
            l.get("systemInfo@sys.com").setHaveUnread(false);
        }
        String fromEmail = (String)request.getSession().getAttribute("fromEmail");
        chatRecordBo.removeApply(email, fromEmail);
    }
}
