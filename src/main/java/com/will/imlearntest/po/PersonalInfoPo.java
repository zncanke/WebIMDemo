package com.will.imlearntest.po;

/**
 * Created by willl on 6/5/16.
 */
public class PersonalInfoPo {
    private String username;
    private int gender;
    private String email;
    private String signature;
    private String pic;
    public void setUsername(String username) {
        this.username = username;
    }
    public String getUsername() {
        return this.username;
    }
    public void setGender(int gender) {
        this.gender = gender;
    }
    public int getGender() {
        return this.gender;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getEmail() {
        return this.email;
    }
    public void setSignature(String signature) {
        this.signature = signature;
    }
    public String getSignature() {
        return signature;
    }
    public String getPic() {
        return pic;
    }
    public void setPic(String pic) {
        this.pic = pic;
    }
}
