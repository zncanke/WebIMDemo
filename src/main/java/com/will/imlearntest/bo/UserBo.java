package com.will.imlearntest.bo;

import com.will.imlearntest.vo.UserStatusVo;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

/**
 * Created by willl on 5/18/16.
 */

@Service
public class UserBo {
    private static Map<String, UserStatusVo> userStatus = new Hashtable<String, UserStatusVo>();
    public void login(String username) {
        UserStatusVo userStatusVo = userStatus.get(username);
        if (userStatusVo == null) {
            userStatusVo = new UserStatusVo();
            userStatus.put(username, userStatusVo);
        }
        userStatusVo.setUsername(username);
        userStatusVo.setStatus(1);
        userStatusVo.setLastHeartBeat(new Date());
    }
}
