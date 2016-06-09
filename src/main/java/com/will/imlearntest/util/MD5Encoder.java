package com.will.imlearntest.util;

import sun.misc.BASE64Encoder;

import java.security.MessageDigest;

public class MD5Encoder {
    public static String encode(String plaintext) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            BASE64Encoder base64en = new BASE64Encoder();
            String ret = base64en.encode(md5.digest(plaintext.getBytes("utf-8")));
            return ret;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    public static void main(String[] args) {
        System.out.println(encode("1"));
    }
}
