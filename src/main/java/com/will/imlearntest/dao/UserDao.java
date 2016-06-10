package com.will.imlearntest.dao;

import com.will.imlearntest.po.FriendshipPo;
import com.will.imlearntest.po.PersonalInfoPo;
import com.will.imlearntest.po.UnreadMessagePo;
import com.will.imlearntest.po.UserPo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDao {
    public List<UserPo> checkUser(@Param("email") String email,
                                  @Param("password") String password);
    public UserPo findUserByEmail(@Param("email") String email);
    public UserPo findUserById(@Param("id") int id);
    public List<Integer> friendList(@Param("userId") int userId, @Param("groupName") String groupName);
    public PersonalInfoPo getPersonalInfo(@Param("email") String email);
    public int updatePersonalInfo(@Param("username") String username, @Param("signature") String signature,
                      @Param("gender") int gender, @Param("email") String email);
    public int updateUser(@Param("username") String username,
                          @Param("email") String email);
    public List<UserPo> searchUser(@Param("userCondition") String userCondition);
    public List<String> getGroupList(@Param("userId") int userId);
    public int addGroup(@Param("userId") int userId,
                        @Param("groupName") String groupName);
    public int setUserStatus(@Param("email") String email,
                             @Param("status") int status);
    public List<UnreadMessagePo> getUnreadFriendList(@Param("toEmail") String toEmail);
    public Integer isFriend(@Param("userId") int userId,
                            @Param("friendId") int friendId);
    public int buildFriendship(@Param("userId") int userId,
                               @Param("friendId") int friendId,
                               @Param("groupName") String groupName);
    public void moveGroup(@Param("userId") int userId,
                          @Param("friendId") int friendId,
                          @Param("groupName") String groupName);
    public String getNowGroup(@Param("userId") int userId,
                              @Param("friendId") int friendId);
    public int removeGroup(@Param("userId") int userId,
                           @Param("groupName") String groupName);
    public int updateGroupName(@Param("userId") int userId,
                               @Param("oriName") String oriName,
                               @Param("newName") String newName);
    public int updateGroupNameInFriendship(@Param("userId") int userId,
                                           @Param("oriName") String oriName,
                                           @Param("newName") String newName);
    public int deleteFriend(@Param("userId") int userId,
                            @Param("friendId") int friendId);

    public int register(@Param("username") String username,
                        @Param("email") String email,
                        @Param("password") String password,
                        @Param("status") int status);
    public int addPersonalInfo(@Param("username") String username,
                               @Param("gender") int gender,
                               @Param("email") String email,
                               @Param("signature") String signature);
}
