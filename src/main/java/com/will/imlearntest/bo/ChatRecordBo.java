package com.will.imlearntest.bo;

import com.will.imlearntest.dao.ChatRecordDao;
import com.will.imlearntest.po.ChatRecordPo;
import com.will.imlearntest.po.UnreadMessagePo;
import com.will.imlearntest.vo.ChatRecordVo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class ChatRecordBo {

    @Autowired
    private ChatRecordDao chatRecordDao;

    public List<ChatRecordVo> recordBetween(String fromEmail, String toEmail) {
//        System.err.println("Before DAO");
        List<ChatRecordPo> list = chatRecordDao.recordBetween(fromEmail, toEmail);


//        System.err.println("After DAO"+total);

        List<ChatRecordVo> chatRecordVoList = new ArrayList<ChatRecordVo>();
        if (CollectionUtils.isNotEmpty(list)) {
            for (ChatRecordPo chatRecordPo : list) {
                ChatRecordVo chatRecordVo = new ChatRecordVo(chatRecordPo);
                chatRecordVoList.add(chatRecordVo);
            }
        }
        Collections.reverse(chatRecordVoList);
        return chatRecordVoList;
    }

    public boolean addChat(String fromEmail, String toEmail, String content) {
        ChatRecordPo chatRecordPo = new ChatRecordPo();
        chatRecordPo.setFromEmail(fromEmail);
        chatRecordPo.setToEmail(toEmail);
        chatRecordPo.setContent(content);
        int insertRowNum = chatRecordDao.insert(chatRecordPo);
        if (insertRowNum != 1) {
            return false;
        }
        return true;
    }

    public boolean addUnreadMessage(int type, String fromEmail, String toEmail, String content) {
        UnreadMessagePo unreadMessage = new UnreadMessagePo();
        unreadMessage.setType(type);
        unreadMessage.setFromEmail(fromEmail);
        unreadMessage.setToEmail(toEmail);
        unreadMessage.setContent(content);
        int ret = chatRecordDao.addUnreadMessage(unreadMessage);
        if (ret == 1)
            return true;
        else
            return false;
    }

    public List<ChatRecordVo> unreadBetween(String fromEmail, String toEmail) {
        List<UnreadMessagePo> list = chatRecordDao.unreadBetween(fromEmail, toEmail);
        List<ChatRecordVo> ret = new ArrayList<ChatRecordVo>();
        ret.clear();
        for (UnreadMessagePo item : list) {
            ChatRecordVo c = new ChatRecordVo();
            c.setContent(item.getContent());
            c.setCreateTime(item.getCreateTime());
            c.setFromEmail(item.getFromEmail());
            c.setToEmail(item.getToEmail());
            ret.add(c);
        }
        chatRecordDao.removeUnreadBetween(fromEmail, toEmail);
        return ret;
    }
}
