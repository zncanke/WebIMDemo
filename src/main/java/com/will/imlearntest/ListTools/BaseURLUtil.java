package com.will.imlearntest.ListTools;

import com.will.imlearntest.vo.UserStatusVo;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by willl on 5/21/16.
 */

public class BaseURLUtil {

    private static Map<Class, String> urlMap = new HashMap<Class, String>();

    static {
        urlMap.put(UserStatusVo.class, "/user/userList?1=1");
    }

    public static String getBaseURL(Class clazz) {
        return urlMap.get(clazz);
    }
}
