package com.will.imlearntest.bo;

import com.will.imlearntest.dao.UserDao;
import com.will.imlearntest.po.FriendshipPo;
import com.will.imlearntest.po.UserPo;
import com.will.imlearntest.vo.UserStatusVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by willl on 5/18/16.
 */

@Service
public class UserBo {

    @Autowired
    private UserDao userDao;

    private static Map<String, UserStatusVo> userStatus = new Hashtable<String, UserStatusVo>();

    /*static {
        UserStatusVo userStatusVo = new UserStatusVo();
        userStatusVo.setUsername("admin");
        userStatusVo.setStatus(1);
        userStatusVo.setLastHeartBeat(new Date());
        userStatus.put("admin", userStatusVo);
    }*/

    public boolean login(String username, String password) {
        List<UserPo> res = userDao.checkUser(username, password);
        if (res.size() == 1)
            return true;
        return false;
    }

    public List<UserStatusVo> listFriends(String username) {
        UserPo user = userDao.findUser(-1, username);
        System.out.println(user.getId());
        List<UserStatusVo> list = new ArrayList<UserStatusVo>();
        list.clear();
        List<FriendshipPo> res = userDao.friendList(user.getId());
        for (FriendshipPo item : res) {
            UserStatusVo tmp = new UserStatusVo();
            UserPo u = userDao.findUser(item.getFriendId(), "");
            tmp.setUsername(u.getUsername());
            tmp.setStatus(u.getStatus());
            tmp.setLastHeartBeat(new Date());
            list.add(tmp);
        }
        return list;
    }

    /*public PageModel<UserStatusVo> listUser(int page, int limit) {
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
    }*/
}
