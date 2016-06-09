package com.will.imlearntest.po;

import java.util.Date;

/**
 * Created by willl on 6/8/16.
 */
public class UnreadMessagePo {
    private int type;
    private Date createTime;
    private String fromEmail;
    private String toEmail;
    private String content;


    public void setType(int type) {
        this.type = type;
    }
    public int getType() {
        return this.type;
    }
    public Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
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
