package com.will.imlearntest.dao;

import com.will.imlearntest.po.FriendshipPo;
import com.will.imlearntest.po.PersonalInfoPo;
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
    public List<FriendshipPo> friendList(@Param("userId") int userId);
    public PersonalInfoPo getPersonalInfo(@Param("email") String email);
    public int updatePersonalInfo(@Param("username") String username, @Param("signature") String signature,
                      @Param("gender") int gender, @Param("email") String email);
    public int updateUser(@Param("username") String username,
                          @Param("email") String email);
}
