package com.will.imlearntest.vo;

public class BaseResultVo {
    private int status;
    private String message;

    public static BaseResultVo success = new BaseResultVo(200, "success");

    public BaseResultVo(int status, String message) {
        this.status = status;
        this.message = message;
    }
    public int getStatus() {
        return status;
    }
    public String getMessage() {
        return message;
    }
}
