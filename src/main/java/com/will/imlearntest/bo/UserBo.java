package com.will.imlearntest.bo;

import com.will.imlearntest.ListTools.PageModel;
import com.will.imlearntest.vo.UserStatusVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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

    public PageModel<UserStatusVo> listUser(int page, int limit) {
        PageModel<UserStatusVo> pageModel = new PageModel<UserStatusVo>();
        pageModel.setClazz(UserStatusVo.class);
        pageModel.setLimit(limit);
        int offset = (page-1) * limit;
        pageModel.setOffset(offset);
        pageModel.setTotal(Integer.MAX_VALUE);
        List<UserStatusVo> userList = new ArrayList<UserStatusVo>();
        for(int i=offset; i<offset+limit; i++ ) {
            UserStatusVo user = new UserStatusVo();
            user.setLastHeartBeat(new Date());
            user.setStatus(1);
            user.setUsername("username" + i);
            userList.add(user);
        }
        pageModel.setData(userList);
        return pageModel;
    }
}
