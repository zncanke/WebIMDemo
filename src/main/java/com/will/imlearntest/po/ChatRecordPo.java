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
    private String fromUserName;
    private String toUserName;
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