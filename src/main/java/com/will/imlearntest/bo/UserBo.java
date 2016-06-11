package com.will.imlearntest.bo;

import com.will.imlearntest.dao.UserDao;
import com.will.imlearntest.po.FriendshipPo;
import com.will.imlearntest.po.PersonalInfoPo;
import com.will.imlearntest.po.UnreadMessagePo;
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
                PersonalInfoPo pi = userDao.getPersonalInfo(p1.getEmail());
                UserStatusVo p2 = new UserStatusVo();
                p2.setEmail(p1.getEmail());
                p2.setStatus(p1.getStatus());
                p2.setUsername(p1.getUsername());
                p2.setHaveUnread(false);
                p2.setPic(pi.getPic());
                p2.setSignature(pi.getSignature());
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

    private boolean isFriend(String email1, String email2) {
        UserPo userId = userDao.findUserByEmail(email1);
        UserPo friendId = userDao.findUserByEmail(email2);
        int count = userDao.isFriend(userId.getId(), friendId.getId());
        return count > 0;
    }

    public List<UserStatusVo> searchUser(String userCondition, String fromEmail) {
        List<UserPo> list = userDao.searchUser(userCondition);
        List<UserStatusVo> ret =  new ArrayList<UserStatusVo>();
        ret.clear();
        for (UserPo item : list) {
            if (isFriend(item.getEmail(), fromEmail))
                continue;
            if (item.getEmail().equals(fromEmail))
                continue;
            PersonalInfoPo p = userDao.getPersonalInfo(item.getEmail());
            UserStatusVo tmp = new UserStatusVo();
            tmp.setUsername(item.getUsername());
            tmp.setEmail(item.getEmail());
            tmp.setPic(p.getPic());
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
        PersonalInfoPo p = userDao.getPersonalInfo(userPo.getEmail());
        ret.setUsername(userPo.getUsername());
        ret.setEmail(userPo.getEmail());
        ret.setStatus(userPo.getStatus());
        ret.setPic(p.getPic());
        ret.setSignature(p.getSignature());
        return ret;
    }

    public Map<String, UserStatusVo> recentList(String email, Map<String, UserStatusVo> ret) {
        List<UnreadMessagePo> list = userDao.getUnreadFriendList(email);
        for (UnreadMessagePo item : list) {
            if (item.getType() >= 110 && item.getType() < 120) {
                ret.get("systemInfo@sys.com").setHaveUnread(true);
                continue;
            }
            UserStatusVo t = ret.get(item.getFromEmail());
            if (t != null) {
                t.setHaveUnread(true);
                continue;
            }
            UserPo u = userDao.findUserByEmail(item.getFromEmail());
            PersonalInfoPo p = userDao.getPersonalInfo(u.getEmail());
            UserStatusVo v = new UserStatusVo();
            v.setHaveUnread(true);
            v.setEmail(u.getEmail());
            v.setStatus(u.getStatus());
            v.setUsername(u.getUsername());
            v.setPic(p.getPic());
            v.setSignature(p.getSignature());
            ret.put(v.getEmail(), v);
        }
        return ret;
    }

    public void buildFriendship(String fromEmail, String toEmail) {
        UserPo u1 = userDao.findUserByEmail(fromEmail);
        UserPo u2 = userDao.findUserByEmail(toEmail);
        userDao.buildFriendship(u1.getId(), u2.getId(), "default");
        userDao.buildFriendship(u2.getId(), u1.getId(), "default");
    }

    public void moveGroup(String fromEmail, String toEmail, String groupName) {
        UserPo u1 = userDao.findUserByEmail(fromEmail);
        UserPo u2 = userDao.findUserByEmail(toEmail);
        userDao.moveGroup(u1.getId(), u2.getId(), groupName);
    }

    public String getNowGroup(String fromEmail, String toEmail) {
        UserPo u1 = userDao.findUserByEmail(fromEmail);
        UserPo u2 = userDao.findUserByEmail(toEmail);
        return userDao.getNowGroup(u1.getId(), u2.getId());
    }

    public int removeGroup(String fromEmail, String groupName) {
        UserPo u = userDao.findUserByEmail(fromEmail);
        return userDao.removeGroup(u.getId(), groupName);
    }

    public int updateGroupName(String email, String oriName, String newName) {
        UserPo u = userDao.findUserByEmail(email);
        userDao.updateGroupNameInFriendship(u.getId(), oriName, newName);
        return userDao.updateGroupName(u.getId(), oriName, newName);
    }

    public int deleteFriend(String fromEmail, String toEmail) {
        UserPo u1 = userDao.findUserByEmail(fromEmail);
        UserPo u2 = userDao.findUserByEmail(toEmail);
        return userDao.deleteFriend(u1.getId(), u2.getId());
    }

    public int register(String username, String email, String password) {
        UserPo u;
        u = userDao.findUserByEmail(email);
        if (u != null)
            return 0;
        userDao.register(username, email, password, 0);
        u = userDao.findUserByEmail(email);
        userDao.addGroup(u.getId(), "default");
        return userDao.addPersonalInfo(username, 0, email, "");
    }

    public int updatePic(String email, String pic) {
        return userDao.updatePic(email, pic);
    }
}
