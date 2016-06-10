package com.will.imlearntest.dao;

import com.will.imlearntest.po.ChatRecordPo;
import com.will.imlearntest.po.UnreadMessagePo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRecordDao {
    public int insert(ChatRecordPo chatRecordPo);

    public Integer count(@Param("fromUserName") String fromUserName, @Param("toUserName") String toUserName);

    public List<ChatRecordPo> recordBetween(@Param("fromEmail") String fromEmail,
                                            @Param("toEmail") String toEmail);

    public int addUnreadMessage(UnreadMessagePo unreadMessagePo);

    public List<UnreadMessagePo> unreadBetween(@Param("fromEmail") String fromEmail,
                                               @Param("toEmail") String toEmail);

    public int removeUnreadBetween(@Param("fromEmail") String fromEmail,
                                   @Param("toEmail") String toEamil);

    public List<String> applyList(@Param("toEmail") String toEmail);

    public int removeApply(@Param("fromEmail") String fromEmail,
                           @Param("toEmail") String toEmail);

    public int deleteRecordsBetween(@Param("fromEmail")String fromEmail,
                                    @Param("toEmail")String toEmail);

    public int deleteUnreadBetween(@Param("fromEmail")String fromEmail,
                                   @Param("toEmail") String toEmail);
}