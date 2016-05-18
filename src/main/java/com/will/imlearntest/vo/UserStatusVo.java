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
    /**
     * 上一次心跳时间
     */
    private Date lastHeartBeat;

    public void setUsername(String username) {
        this.username = username;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setLastHeartBeat(Date lastHeartBeat) {
        this.lastHeartBeat = lastHeartBeat;
    }

}
