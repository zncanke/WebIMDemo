package com.will.imlearntest.dao;

import com.will.imlearntest.po.UserPo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDao {
    public List<UserPo> checkUser(@Param("username") String username,
                                  @Param("password") String password);
}
