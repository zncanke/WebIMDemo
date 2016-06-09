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

    public Map<String, Map<String, UserStatusVo>> listFriends(String email) {
        UserPo user = userDao.findUserByEmail(email);
        List<String> groupList = userDao.getGroupList(user.getId());
        Map<String, Map<String, UserStatusVo>> list = new HashMap<String, Map<String, UserStatusVo>>();
        list.clear();
        for (String group : groupList) {
            Map<String, UserStatusVo> child = new HashMap<String, UserStatusVo>();
            child.clear();
            List<Integer> tmp = userDao.friendList(user.getId(), group);
            for (int i : tmp) {
                UserPo p1 = userDao.findUserById(i);
                UserStatusVo p2 = new UserStatusVo();
                p2.setEmail(p1.getEmail());
                p2.setStatus(p1.getStatus());
                p2.setUsername(p1.getUsername());
                p2.setHaveUnread(false);
                child.put(p2.getEmail(), p2);
            }
            list.put(group, child);
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

    public List<UserStatusVo> searchUser(String userCondition) {
        List<UserPo> list = userDao.searchUser(userCondition);
        List<UserStatusVo> ret =  new ArrayList<UserStatusVo>();
        ret.clear();
        for (UserPo item : list) {
            UserStatusVo tmp = new UserStatusVo();
            tmp.setUsername(item.getUsername());
            tmp.setEmail(item.getEmail());
            ret.add(tmp);
        }
        return ret;
    }

    public int addGroup(String email, String groupName) {
        UserPo user = userDao.findUserByEmail(email);
        List<String> groupList = userDao.getGroupList(user.getId());
        if (groupList.contains(groupName))
            return 0;
        userDao.addGroup(user.getId(), groupName);
        return 1;
    }

    public void userLogin(String email) {
        userDao.setUserStatus(email, 1);
    }

    public void userLogout(String email) {
        userDao.setUserStatus(email, 0);
    }

    public int getUserStatus(String email) {
        UserPo userPo = userDao.findUserByEmail(email);
        return userPo.getStatus();
    }

    public UserStatusVo getUserByEmail(String email) {
        UserStatusVo ret = new UserStatusVo();
        UserPo userPo = userDao.findUserByEmail(email);
        ret.setUsername(userPo.getUsername());
        ret.setEmail(userPo.getEmail());
        ret.setStatus(userPo.getStatus());
        return ret;
    }

    public Map<String, UserStatusVo> recentList(String email, Map<String, UserStatusVo> ret) {
        List<String> list = userDao.getUnreadFriendList(email);
        for (String item : list) {
            if (ret.get(item) != null)
                continue;
            UserPo u = userDao.findUserByEmail(item);
            UserStatusVo v = new UserStatusVo();
            v.setHaveUnread(true);
            v.setEmail(u.getEmail());
            v.setStatus(u.getStatus());
            v.setUsername(u.getUsername());
            ret.put(v.getEmail(), v);
        }
        return ret;
    }
}
