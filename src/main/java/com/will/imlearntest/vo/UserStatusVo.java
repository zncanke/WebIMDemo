package com.will.imlearntest.vo;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * Created by willl on 5/18/16.
 */

public class UserStatusVo {
    /**
     * 用户名
     */
    private String username;
    /**
     * 状态
     */
    private int status;
    private String email;
    private boolean haveUnread;
    private String signature;
    private String pic;

    public void setUsername(String username) {
        this.username = username;
    }
    public String getUsername() {
        return username;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public int getStatus() {
        return status;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getEmail() {
        return email;
    }
    public void setHaveUnread(boolean haveUnread) {
        this.haveUnread = haveUnread;
    }
    public boolean getHaveUnread() {
        return this.haveUnread;
    }
    public String getSignature() {
        return signature;
    }
    public void setSignature(String signature) {
        this.signature = signature;
    }
    public String getPic() {
        return pic;
    }
    public void setPic(String pic) {
        this.pic = pic;
    }
}
