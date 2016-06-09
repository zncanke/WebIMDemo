package com.will.imlearntest.websocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.will.imlearntest.bo.ChatRecordBo;
import com.will.imlearntest.bo.UserBo;
import com.will.imlearntest.vo.UserStatusVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Controller
@ServerEndpoint(value="/websocket", configurator=GetHttpSessionConfigurator.class)
public class MyWebSocket {
    //WebSocket操作类型
    private final int UPDATE_PERSONAL_INFO = 1;
    private final int SEND_MESSAGE = 2;
    private final int USER_LOGIN = 3;
    private final int USER_LOGOUT = 4;

    //消息类型
    private final int TEXT_MESSAGE_RECENT_OPEN = 100;
    private final int TEXT_MESSAGE_RECENT_CLOSE = 101;
    private final int TEXT_MESSAGE_FRIEND_OPEN = 102;
    private final int TEXT_MESSAGE_FRIEND_CLOSE = 103;
    private final int FRIEND_APPLY = 110;

    private static ConcurrentHashMap<String, MyWebSocket> webSocketMap = new ConcurrentHashMap<String, MyWebSocket>();
    private Session session;
    private HttpSession httpSession;

    public MyWebSocket() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @Autowired
    private ChatRecordBo chatRecordBo;
    @Autowired
    private UserBo userBo;

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        HttpSession httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        this.httpSession = httpSession;
        String email = (String)httpSession.getAttribute("fromEmail");
        this.session = session;
        webSocketMap.put(email, this);
    }

    @OnClose
    public void onClose() {
        webSocketMap.remove(this);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        System.err.println("some mistakes occurred");
        throwable.printStackTrace();
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        JSONObject jsonObject = (JSONObject)JSON.parse(message);
        int op = judgeType(jsonObject.getString("type"));
        switch (op) {
            case UPDATE_PERSONAL_INFO: updatePersonalInfo(jsonObject); break;
            case SEND_MESSAGE: sendMessage(jsonObject); break;
            case USER_LOGIN: userLogin(); break;
            case USER_LOGOUT: userLogout(); break;
        }
    }

    private int judgeType(String type) {
        if (type.equals("updatePersonalInfo"))
            return UPDATE_PERSONAL_INFO;
        else if (type.equals("sendMessage"))
            return SEND_MESSAGE;
        else if (type.equals("userLogin"))
            return USER_LOGIN;
        else if (type.equals("userLogout"))
            return USER_LOGOUT;
        return 0;
    }

    private void updatePersonalInfo(JSONObject jsonObject) {
        String username = jsonObject.getString("thename");
        String signature = jsonObject.getString("thesign");
        String gender = jsonObject.getString("thegender");
        String fromEmail = (String)httpSession.getAttribute("fromEmail");
        String detailEmail = (String)httpSession.getAttribute("detailEmail");
        if (fromEmail.equals(detailEmail))
            userBo.updatePersonalinfo(username, signature, gender, fromEmail);
    }

    private void sendMessage(JSONObject jsonObject) {
        String fromEmail = jsonObject.getString("fromEmail");
        String toEmail = jsonObject.getString("toEmail");
        String content = jsonObject.getString("content");
        MyWebSocket myWebSocket = webSocketMap.get(toEmail);

        int type;
        String listStatus = (String)myWebSocket.httpSession.getAttribute("listStatus");
        String boxStatus = (String)myWebSocket.httpSession.getAttribute("boxStatus");
        type = 100;
        type += listStatus.equals("friendList") ? 2 : 0;
        type += boxStatus.equals(fromEmail) ? 0 : 1;

        jsonObject.put("type", type);

        chatRecordBo.addChat(fromEmail, toEmail, content);
        if (type != 100 && type != 102)
            chatRecordBo.addUnreadMessage(TEXT_MESSAGE_RECENT_CLOSE, fromEmail, toEmail, content);

        if (userBo.getUserStatus(toEmail) == 1) {
            try {
                myWebSocket.session.getBasicRemote().sendText(jsonObject.toJSONString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void userLogin() {
        String fromEmail = (String)httpSession.getAttribute("fromEmail");
        userBo.userLogin(fromEmail);
    }

    private void userLogout() {
        String fromEmail = (String)httpSession.getAttribute("fromEmail");
        userBo.userLogout(fromEmail);
    }
}
