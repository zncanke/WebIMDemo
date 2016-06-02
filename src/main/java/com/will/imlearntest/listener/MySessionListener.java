package com.will.imlearntest.listener;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.HashSet;

public class MySessionListener implements HttpSessionListener{
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {

    }

    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        HttpSession session = httpSessionEvent.getSession();
//        ServletContext application = session.getServletContext();
//        HashSet sessions = (HashSet) application.getAttribute("sessions");
        System.out.println(session.getAttribute("fromUserName"));
//        sessions.remove(session);
    }
}
