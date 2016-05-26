package com.will.imlearntest.po;

/**
 * Created by willl on 5/26/16.
 */
public class UserPo {
    private int id;
    private String username;
    private String password;
    private int status;

    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getUsername() {
        return username;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getPassword() {
        return password;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public int getStatus() {
        return status;
    }
}
