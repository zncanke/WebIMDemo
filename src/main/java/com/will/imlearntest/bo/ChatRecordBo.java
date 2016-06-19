package com.will.imlearntest.bo;

import com.will.imlearntest.dao.ChatRecordDao;
import com.will.imlearntest.dao.UserDao;
import com.will.imlearntest.po.ChatRecordPo;
import com.will.imlearntest.po.PersonalInfoPo;
import com.will.imlearntest.po.UnreadMessagePo;
import com.will.imlearntest.po.UserPo;
import com.will.imlearntest.vo.ChatRecordVo;
import com.will.imlearntest.vo.UserStatusVo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ChatRecordBo {

    @Autowired
    private ChatRecordDao chatRecordDao;

    @Autowired
    private UserDao userDao;

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

    public Map<String, UserStatusVo> applyList(String email) {
        List<String> list = chatRecordDao.applyList(email);
        Map<String, UserStatusVo> ret = new HashMap<String, UserStatusVo>();
        ret.clear();
        for (String e : list) {
            UserPo u = userDao.findUserByEmail(e);
            PersonalInfoPo p = userDao.getPersonalInfo(u.getEmail());
            UserStatusVo v = new UserStatusVo();
            v.setEmail(u.getEmail());
            v.setUsername(u.getUsername());
            v.setPic(p.getPic());
            ret.put(e, v);
        }
        return ret;
    }

    public void removeApply(String fromEmail, String toEmail) {
        chatRecordDao.removeApply(fromEmail, toEmail);
    }

    public void deleteRecordsBetween(String fromEmail, String toEmail) {
        chatRecordDao.deleteRecordsBetween(fromEmail, toEmail);
        chatRecordDao.deleteUnreadBetween(fromEmail, toEmail);
    }

    public List<ChatRecordVo> getAllRecordsBetween(String fromEmail, String toEmail) {
        List<ChatRecordPo> list;
        list = chatRecordDao.getAllRecordsBetween(fromEmail, toEmail);
        List<ChatRecordVo> ret = new ArrayList<ChatRecordVo>();
        ret.clear();
        for (ChatRecordPo p : list) {
            ChatRecordVo v = new ChatRecordVo();
            v.setContent(p.getContent());
            v.setCreateTime(p.getCreateTime());
            v.setFromEmail(p.getFromEmail());
            v.setToEmail(p.getToEmail());
            ret.add(v);
        }
        return ret;
    }
}
