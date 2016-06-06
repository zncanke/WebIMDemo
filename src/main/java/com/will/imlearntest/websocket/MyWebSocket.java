package com.will.imlearntest.websocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.SymbolTable;
import com.will.imlearntest.bo.ChatRecordBo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.ConcurrentHashMap;


@Controller
@ServerEndpoint(value="/websocket", configurator=GetHttpSessionConfigurator.class)
public class MyWebSocket {
    private static ConcurrentHashMap<String, MyWebSocket> webSocketMap = new ConcurrentHashMap<String, MyWebSocket>();
    private Session session;

    public MyWebSocket() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @Autowired
    private ChatRecordBo chatRecordBo;

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        HttpSession httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
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
        MyWebSocket myWebSocket = webSocketMap.get(jsonObject.getString("toEmail"));
        while (!chatRecordBo.addChat(jsonObject.getString("fromEmail"),
                             jsonObject.getString("toEmail"),
                             jsonObject.getString("content")))
            chatRecordBo.addChat(jsonObject.getString("fromEmail"),
                    jsonObject.getString("toEmail"),
                    jsonObject.getString("content"));

//        System.out.println(jsonObject);
//        System.out.println(myWebSocket);
        try {
            if (myWebSocket != null)
                myWebSocket.session.getBasicRemote().sendText(jsonObject.toJSONString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
