package com.will.imlearntest.dao;

import com.will.imlearntest.po.ChatRecordPo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRecordDao {
    public int insert(ChatRecordPo chatRecordPo);

    public List<ChatRecordPo> list(@Param("fromUserName") String fromUserName, @Param("toUserName") String toUserName,
                                   @Param("offset") int offset, @Param("limit") int limit);

    public Integer count(@Param("fromUserName") String fromUserName, @Param("toUserName") String toUserName);
    public List<ChatRecordPo> recordBetween(@Param("fromUserName") String fromUserName,
                                            @Param("toUserName") String toUserName);
}
