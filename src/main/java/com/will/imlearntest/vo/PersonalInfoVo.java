package com.will.imlearntest.vo;

import com.will.imlearntest.po.PersonalInfoPo;

/**
 * Created by willl on 6/5/16.
 */
public class PersonalInfoVo {
    private String username;
    private int gender;
    private String email;
    private String signature;
    public PersonalInfoVo(PersonalInfoPo personalInfoPo) {
        this.username = personalInfoPo.getUsername();
        StringBuffer tmp = new StringBuffer("Email:");
        tmp.append(personalInfoPo.getEmail());
        this.email = tmp.toString();
        this.signature = personalInfoPo.getSignature();
        this.gender = personalInfoPo.getGender();
    }
    public String getEmail() {
        return this.email;
    }
    public int getGender() {
        return this.gender;
    }
    public String getSignature() {
        return this.signature;
    }
    public String getUsername() {
        return this.username;
    }
}
