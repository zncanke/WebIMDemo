package com.will.imlearntest.vo;

import com.will.imlearntest.po.ChatRecordPo;

import java.util.Date;

public class ChatRecordVo {
    private int id;
    private Date createTime;
    private Date updateTime;
    private int version;
    /**
     * 发送者用户名
     */
    private String fromUserName;
    /**
     * 接受者用户名
     */
    private String toUserName;
    /**
     * 聊天内容
     */
    private String content;

    public ChatRecordVo(ChatRecordPo chatRecordPo) {
        this.content = chatRecordPo.getContent();
        this.createTime = chatRecordPo.getCreateTime();
        this.fromUserName = chatRecordPo.getFromUserName();
        this.id = chatRecordPo.getId();
        this.toUserName = chatRecordPo.getToUserName();
        this.updateTime = chatRecordPo.getUpdateTime();
        this.version = chatRecordPo.getVersion();
    }

    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    public Date getCreateTime() {
        return createTime;
    }
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
    public Date getUpdateTime() {
        return updateTime;
    }
    public void setVersion(int version) {
        this.version = version;
    }
    public int getVersion() {
        return version;
    }
    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }
    public String getFromUserName() {
        return fromUserName;
    }
    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }
    public String getToUserName() {
        return toUserName;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getContent() {
        return content;
    }
}
