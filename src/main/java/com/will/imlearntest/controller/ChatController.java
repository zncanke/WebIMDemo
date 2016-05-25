package com.will.imlearntest.controller;

import com.will.imlearntest.ListTools.PageModel;
import com.will.imlearntest.bo.ChatRecordBo;
import com.will.imlearntest.vo.ChatRecordVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("chat")
public class ChatController {

    @Autowired
    private ChatRecordBo chatRecordBo;

    @RequestMapping("chat")
    public String chat(@ModelAttribute("fromUserName") String fromUserName,
                       @ModelAttribute("toUserName") String toUserName, HttpServletRequest request,
                       HttpServletResponse response) {

//        System.err.println("chatchat:" + fromUserName + " " + toUserName);

        PageModel<ChatRecordVo> chatRecordPageModel = chatRecordBo.list(fromUserName, toUserName, 0, 10);

        System.err.println(chatRecordPageModel.getTotal());

        request.setAttribute("pageModel", chatRecordPageModel);

        return "chat/chat";
    }

    @RequestMapping("chatRecordList")
    public String chatRecordList(@ModelAttribute("page") int page, @ModelAttribute("limit") int limit,
                                 @ModelAttribute("fromUserName") String fromUserName,
                                 @ModelAttribute("toUserName") String toUserName, HttpServletRequest request,
                                 HttpServletResponse response) {
        if (page <= 0) {
            page = 1;
        }

        System.err.println("chatlog:" + fromUserName + " " + toUserName);

        PageModel<ChatRecordVo> chatRecordPageModel = chatRecordBo.list(fromUserName, toUserName, (page - 1) * limit,
                limit);
        request.setAttribute("pageModel", chatRecordPageModel);
        return "chat/chatlog";
    }
}
