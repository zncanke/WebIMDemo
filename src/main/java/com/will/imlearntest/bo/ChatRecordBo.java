package com.will.imlearntest.bo;

import com.will.imlearntest.ListTools.PageModel;
import com.will.imlearntest.dao.ChatRecordDao;
import com.will.imlearntest.po.ChatRecordPo;
import com.will.imlearntest.vo.ChatRecordVo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ChatRecordBo {

    @Autowired
    private ChatRecordDao chatRecordDao;

    public PageModel<ChatRecordVo> list(String fromUserName, String toUserName, int offset, int limit) {
//        System.err.println("Before DAO");
        List<ChatRecordPo> list = chatRecordDao.list(fromUserName, toUserName, offset, limit);
        Integer total = chatRecordDao.count(fromUserName, toUserName);

//        System.err.println("After DAO"+total);

        List<ChatRecordVo> chatRecordVoList = new ArrayList<ChatRecordVo>();
        if (CollectionUtils.isNotEmpty(list) && total != null && total != 0) {
            for (ChatRecordPo chatRecordPo : list) {
                ChatRecordVo chatRecordVo = new ChatRecordVo(chatRecordPo);
                chatRecordVoList.add(chatRecordVo);
            }
        }
        PageModel<ChatRecordVo> pageModel = new PageModel<ChatRecordVo>();
        pageModel.setClazz(ChatRecordVo.class);
        pageModel.setData(chatRecordVoList);
        pageModel.setLimit(limit);
        pageModel.setOffset(offset);
        pageModel.setTotal(total);
        pageModel.addParam("fromUserName", fromUserName);
        pageModel.addParam("toUserName", toUserName);
        return pageModel;
    }

    public boolean addChat(String fromUsername, String toUsername, String content) {
//        System.err.println("addChat1");
        ChatRecordPo chatRecordPo = new ChatRecordPo();
//        System.err.println("addChat2");
        chatRecordPo.setFromUserName(fromUsername);
//        System.err.println("addChat3");
        chatRecordPo.setToUserName(toUsername);
//        System.err.println("addChat4");
        chatRecordPo.setContent(content);
//        System.err.println("before insert");
        int insertRowNum = chatRecordDao.insert(chatRecordPo);
//        System.err.println("after insert");
        if (insertRowNum != 1) {
            return false;
        }
        return true;
    }
}
