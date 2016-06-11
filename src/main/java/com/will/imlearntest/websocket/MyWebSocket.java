package com.will.imlearntest.websocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.will.imlearntest.bo.ChatRecordBo;
import com.will.imlearntest.bo.UserBo;
import com.will.imlearntest.vo.UserStatusVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.json.Json;
import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.net.URLDecoder;
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
    private final int FRIEND_APPLY = 5;
    private final int ALLOW_APPLY = 6;
    private final int DELETE_FRIEND = 7;

    //消息类型
    private final int TEXT_MESSAGE_RECENT_OPEN = 100;
    private final int TEXT_MESSAGE_RECENT_CLOSE = 101;
    private final int TEXT_MESSAGE_FRIEND_OPEN = 102;
    private final int TEXT_MESSAGE_FRIEND_CLOSE = 103;
    private final int FRIEND_APPLY_RECENT_OPEN = 110;
    private final int FRIEND_APPLY_RECENT_CLOSE = 111;
    private final int FRIEND_APPLY_FRIEND_OPEN = 112;
    private final int FRIEND_APPLY_FRIEND_CLOSE = 113;

    //好友申请接受
    private final int NEED_TO_REFRESH_FRIENDLIST = 120;

    //删除好友
    private final int DELETE_FRIEND_RECENT_OPEN = 130;
    private final int DELETE_FRIEND_RECENT_CLOSE = 131;
    private final int DELETE_FRIEND_FRIEND_OPEN = 132;
    private final int DELETE_FRIEND_FRIEND_CLOSE = 133;

    //登入登出
    private final int IN_MESSAGE = 140;
    private final int OUT_MESSAGE = 150;

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
            case USER_LOGIN: userLogin(jsonObject); break;
            case USER_LOGOUT: userLogout(jsonObject); break;
            case FRIEND_APPLY: friendApply(jsonObject); break;
            case ALLOW_APPLY: allowApply(jsonObject); break;
            case DELETE_FRIEND: deleteFriend(jsonObject); break;
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
        else if (type.equals("friendApply"))
            return FRIEND_APPLY;
        else if (type.equals("allowApply"))
            return ALLOW_APPLY;
        else if (type.equals("deleteFriend"))
            return DELETE_FRIEND;
        return 0;
    }

    private void updatePersonalInfo(JSONObject jsonObject){
        String username = jsonObject.getString("thename");
        String signature = jsonObject.getString("thesign");
        String group = jsonObject.getString("thegroup");
        try {
            username = URLDecoder.decode(username, "UTF-8");
            signature = URLDecoder.decode(signature, "UTF-8");
            group = URLDecoder.decode(group, "UTF-8");
        } catch (Exception e){
            e.printStackTrace();
        }
        String gender = jsonObject.getString("thegender");
        String fromEmail = (String)httpSession.getAttribute("fromEmail");
        String detailEmail = (String)httpSession.getAttribute("detailEmail");
        if (fromEmail.equals(detailEmail))
            userBo.updatePersonalinfo(username, signature, gender, fromEmail);
        else {
            userBo.moveGroup(fromEmail, detailEmail, group);
            jsonObject.put("type", NEED_TO_REFRESH_FRIENDLIST);
            try {
                this.session.getBasicRemote().sendText(jsonObject.toJSONString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void sendMessage(JSONObject jsonObject) {
        String fromEmail = jsonObject.getString("fromEmail");
        String toEmail = jsonObject.getString("toEmail");
        String content = jsonObject.getString("content");
        try {
            content = URLDecoder.decode(content, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        MyWebSocket myWebSocket = webSocketMap.get(toEmail);

        chatRecordBo.addChat(fromEmail, toEmail, content);

        if (userBo.getUserStatus(toEmail) == 1) {
            int type;
            String listStatus = (String)myWebSocket.httpSession.getAttribute("listStatus");
            String boxStatus = (String)myWebSocket.httpSession.getAttribute("boxStatus");
            type = 100;
            type += listStatus.equals("friendList") ? 2 : 0;
            type += boxStatus.equals(fromEmail) ? 0 : 1;

            jsonObject.put("type", type);

            if (type != 100 && type != 102)
                chatRecordBo.addUnreadMessage(TEXT_MESSAGE_RECENT_CLOSE, fromEmail, toEmail, content);

            try {
                myWebSocket.session.getBasicRemote().sendText(jsonObject.toJSONString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            chatRecordBo.addUnreadMessage(TEXT_MESSAGE_RECENT_CLOSE, fromEmail, toEmail, content);
        }
    }

    private void userLogin(JSONObject jsonObject) {
        String fromEmail = (String)httpSession.getAttribute("fromEmail");
        userBo.userLogin(fromEmail);
        sendInOutMessage(jsonObject, IN_MESSAGE, fromEmail);
    }

    private void userLogout(JSONObject jsonObject) {
        String fromEmail = (String)httpSession.getAttribute("fromEmail");
        userBo.userLogout(fromEmail);
        sendInOutMessage(jsonObject, OUT_MESSAGE, fromEmail);
    }

    private void sendInOutMessage(JSONObject jsonObject, int type, String fromEmail) {
        Map<String, Map<String, UserStatusVo>> list = (Map<String, Map<String, UserStatusVo>>)httpSession.getAttribute("friendList");
        for (String g : list.keySet()) {
            Map<String, UserStatusVo> group = list.get(g);
            for (String toEmail : group.keySet()) {
                if (userBo.getUserStatus(toEmail) == 1) {
                    MyWebSocket myWebSocket = webSocketMap.get(toEmail);
                    String listStatus = (String)myWebSocket.httpSession.getAttribute("listStatus");

                    type += listStatus.equals("friendList") ? 2 : 0;

                    jsonObject.put("type", type);
                    jsonObject.put("email", fromEmail);
                    try {
                        myWebSocket.session.getBasicRemote().sendText(jsonObject.toJSONString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void friendApply(JSONObject jsonObject) {
        String fromEmail = (String) httpSession.getAttribute("fromEmail");
        String toEmail = jsonObject.getString("toEmail");
        MyWebSocket myWebSocket = webSocketMap.get(toEmail);


        if (userBo.getUserStatus(toEmail) == 1) {
            int type;
            String listStatus = (String)myWebSocket.httpSession.getAttribute("listStatus");
            String boxStatus = (String)myWebSocket.httpSession.getAttribute("boxStatus");
            type = 110;
            type += listStatus.equals("friendList") ? 2 : 0;
            type += boxStatus.equals("systemInfo@sys.com") ? 0 : 1;

            jsonObject.put("type", type);

//            if (type != 110 && type != 112)
            chatRecordBo.addUnreadMessage(FRIEND_APPLY_FRIEND_CLOSE, fromEmail, toEmail, "");
            try {
                myWebSocket.session.getBasicRemote().sendText(jsonObject.toJSONString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            chatRecordBo.addUnreadMessage(FRIEND_APPLY_FRIEND_CLOSE, fromEmail, toEmail, "");
        }
    }

    private void allowApply(JSONObject jsonObject) {
        String fromEmail = jsonObject.getString("fromEmail");
        String toEmail = (String)httpSession.getAttribute("fromEmail");
        Map<String, Map<String, UserStatusVo>> list =
                (Map<String, Map<String, UserStatusVo>>)httpSession.getAttribute("friendList");
        Map<String, UserStatusVo> defaultGroup = list.get("default");
        defaultGroup.put(fromEmail, userBo.getUserByEmail(fromEmail));
        userBo.buildFriendship(fromEmail, toEmail);
        MyWebSocket myWebSocket = webSocketMap.get(fromEmail);
        try {
            String listStatus = (String)httpSession.getAttribute("listStatus");
            if (listStatus.equals("friendList")) {
                jsonObject.put("type", NEED_TO_REFRESH_FRIENDLIST);
                this.session.getBasicRemote().sendText(jsonObject.toJSONString());
            }
            if (myWebSocket != null) {
                String status = (String)myWebSocket.httpSession.getAttribute("listStatus");
                if (status.equals("friendList")) {
                    jsonObject.put("type", NEED_TO_REFRESH_FRIENDLIST);
                    myWebSocket.session.getBasicRemote().sendText(jsonObject.toJSONString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteFriend(JSONObject jsonObject) {
        String fromEmail = (String) httpSession.getAttribute("fromEmail");
        String toEmail = jsonObject.getString("toEmail");
        toEmail = toEmail.substring(6);
        jsonObject.put("toEmail", toEmail);
        dealDelete(jsonObject, httpSession, session);
        MyWebSocket myWebSocket = webSocketMap.get(toEmail);
        jsonObject.put("toEmail", fromEmail);
        dealDelete(jsonObject, (myWebSocket == null) ? null : myWebSocket.httpSession,
                    (myWebSocket == null) ? null : myWebSocket.session);
        userBo.deleteFriend(fromEmail, toEmail);
        chatRecordBo.deleteRecordsBetween(fromEmail, toEmail);
    }

    private void dealDelete(JSONObject jsonObject, HttpSession httpSession, Session session) {
        if (httpSession == null)
            return;
        String fromEmail = (String)httpSession.getAttribute("fromEmail");
        String toEmail = jsonObject.getString("toEmail");
        String listStatus = (String) httpSession.getAttribute("listStatus");
        String boxStatus = (String) httpSession.getAttribute("boxStatus");
        int type = 130;
        type += listStatus.equals("friendList") ? 2 : 0;
        type += boxStatus.equals(toEmail) ? 0 : 1;

        jsonObject.put("type", type);
        httpSession.setAttribute("boxStatus", "nothing");

        Map<String, UserStatusVo> recent = (Map<String, UserStatusVo>)httpSession.getAttribute("recentList");
        recent.remove(toEmail);

        String group = userBo.getNowGroup(fromEmail, toEmail);
        Map<String, Map<String, UserStatusVo>> list = (Map<String, Map<String, UserStatusVo>>)httpSession.getAttribute("friendList");
        list.get(group).remove(toEmail);

        try {
            session.getBasicRemote().sendText(jsonObject.toJSONString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
