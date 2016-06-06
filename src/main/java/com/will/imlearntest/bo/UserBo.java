package com.will.imlearntest.bo;

import com.will.imlearntest.dao.UserDao;
import com.will.imlearntest.po.FriendshipPo;
import com.will.imlearntest.po.PersonalInfoPo;
import com.will.imlearntest.po.UserPo;
import com.will.imlearntest.vo.PersonalInfoVo;
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

    public boolean login(String email, String password) {
        List<UserPo> res = userDao.checkUser(email, password);
        if (res.size() == 1)
            return true;
        return false;
    }

    public List<UserStatusVo> listFriends(String email) {
        UserPo user = userDao.findUserByEmail(email);
//        System.out.println(user.getId());
        List<UserStatusVo> list = new ArrayList<UserStatusVo>();
        list.clear();
        List<FriendshipPo> res = userDao.friendList(user.getId());
        for (FriendshipPo item : res) {
            UserStatusVo tmp = new UserStatusVo();
            UserPo u = userDao.findUserById(item.getFriendId());
            tmp.setUsername(u.getUsername());
            tmp.setStatus(u.getStatus());
            tmp.setLastHeartBeat(new Date());
            tmp.setEmail(u.getEmail());
            list.add(tmp);
        }
        return list;
    }

    public PersonalInfoVo getPersonalInfo(String email) {
        PersonalInfoPo personalInfoPo = userDao.getPersonalInfo(email);
        PersonalInfoVo ret = new PersonalInfoVo(personalInfoPo);
        return ret;
    }

    public String getUsernameByEmail(String email) {
        UserPo userPo = userDao.findUserByEmail(email);
        return userPo.getUsername();
    }

    public void updatePersonalinfo(String username, String signature, String gender, String email) {
        int tmp = gender.equals("female") ? 1 : 0;
        userDao.updatePersonalInfo(username, signature, tmp, email);
        userDao.updateUser(username, email);
    }
}
