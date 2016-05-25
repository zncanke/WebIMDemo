package com.will.imlearntest.bo;

import com.will.imlearntest.po.ChatRecordPo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ChatBo {
    @Autowired
    private ChatRecordBo chatRecordBo;
    private static Map<String, Integer> chatStatus = new HashMap<String, Integer>();

    public void sendMessage(String fromUserName, String toUserName, String content) {
        String chatStatusKey = chatStatusKey(fromUserName, toUserName);
        synchronized (chatStatus) {
            Integer status = chatStatus.get(chatStatusKey);
            if (status == null) {
                status = 0;
                chatStatus.put(chatStatusKey, status);
            }
//            System.err.println("before addChat"+content);
            chatRecordBo.addChat(fromUserName, toUserName, content);
            chatStatus.put(chatStatusKey, 1);
        }
    }
    private String chatStatusKey(String fromUserName, String toUserName) {
        return fromUserName + "@" + toUserName;
    }

    public boolean acceptMessage(String fromUserName, String toUserName) {
        String chatStatusKey = chatStatusKey(toUserName, fromUserName);
        int times = 0;
        do {
            synchronized (chatStatus) {
                Integer status = chatStatus.get(chatStatusKey);
                if (status == null) {
                    status = 0;
                    chatStatus.put(chatStatusKey, status);
                }

                if (status == 1) {
                    chatStatus.put(chatStatusKey, 0);
                    return true;
                }
            }

            times++;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (times < 30);

        return false;
    }
}

