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
    private String fromEmail;
    /**
     * 接受者用户名
     */
    private String toEmail;
    /**
     * 聊天内容
     */
    private String content;

    public ChatRecordVo(){}

    public ChatRecordVo(ChatRecordPo chatRecordPo) {
        this.content = chatRecordPo.getContent();
        this.createTime = chatRecordPo.getCreateTime();
        this.fromEmail = chatRecordPo.getFromEmail();
        this.id = chatRecordPo.getId();
        this.toEmail = chatRecordPo.getToEmail();
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
    public void setFromEmail(String fromEmail) {
        this.fromEmail = fromEmail;
    }
    public String getFromEmail() {
        return fromEmail;
    }
    public void setToEmail(String toEmail) {
        this.toEmail = toEmail;
    }
    public String getToEmail() {
        return toEmail;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getContent() {
        return content;
    }
}
