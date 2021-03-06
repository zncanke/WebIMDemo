package com.will.imlearntest.po;

import java.util.Date;

/**
 * Created by willl on 5/25/16.
 */
public class ChatRecordPo {
    private int id;
    private Date createTime;
    private Date updateTime;
    private int version;
    private String fromEmail;
    private String toEmail;
    private String content;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
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
