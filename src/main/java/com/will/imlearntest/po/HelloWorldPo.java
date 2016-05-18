package com.will.imlearntest.po;

import java.util.Date;

public class HelloWorldPo {
    private int id;
    private Date createTime;
    private Date updateTime;
    private int version;
    private String hello;

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
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
    public int getVersion() {
        return version;
    }
    public void setVersion(int version) {
        this.version = version;
    }
    public String getHello() {
        return hello;
    }
    public void setHello(String hello) {
        this.hello = hello;
    }
}
