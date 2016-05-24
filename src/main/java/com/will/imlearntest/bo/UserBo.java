package com.will.imlearntest.bo;

import com.will.imlearntest.ListTools.PageModel;
import com.will.imlearntest.vo.UserStatusVo;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by willl on 5/18/16.
 */

@Service
public class UserBo {
    private static Map<String, UserStatusVo> userStatus = new Hashtable<String, UserStatusVo>();

    static {
        UserStatusVo userStatusVo = new UserStatusVo();
        userStatusVo.setUsername("admin");
        userStatusVo.setStatus(1);
        userStatusVo.setLastHeartBeat(new Date());
        userStatus.put("admin", userStatusVo);
    }

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

    public PageModel<UserStatusVo> listUser(int page, int limit) {
        int offset = (page-1) * limit;
        List<UserStatusVo> userStatusList = new ArrayList<UserStatusVo>();
        int i = 0;
        for (String username : userStatus.keySet()) {
            if (i >= offset && i < offset + limit) {
                userStatusList.add(userStatus.get(username));
            }
            i++;
        }

        PageModel<UserStatusVo> pageModel = new PageModel<UserStatusVo>();
        pageModel.setClazz(UserStatusVo.class);
        pageModel.setData(userStatusList);
        pageModel.setLimit(limit);
        pageModel.setOffset(offset);
        pageModel.setTotal(userStatus.size());

        return pageModel;
    }
}
