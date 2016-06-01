package com.will.imlearntest.websocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint(value="/websocket", configurator=GetHttpSessionConfigurator.class)
public class MyWebSocket {
    private static ConcurrentHashMap<String, MyWebSocket> webSocketMap = new ConcurrentHashMap<String, MyWebSocket>();
    private Session session;

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        HttpSession httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        String username = (String)httpSession.getAttribute("username");
        System.out.println(username);
        this.session = session;
        webSocketMap.put(username, this);
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
        System.out.println(jsonObject);
        /*MyWebSocket myWebSocket = webSocketMap.get(jsonObject.getString("toUsername"));
        jsonObject.remove("toUsername");
        try {
            myWebSocket.session.getBasicRemote().sendText(jsonObject.toJSONString());
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }
}
