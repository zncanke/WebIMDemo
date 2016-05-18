package com.will.imlearntest.dao;

import com.will.imlearntest.po.HelloWorldPo;
import org.springframework.stereotype.Repository;

@Repository
public interface HelloWorldDao {
    public HelloWorldPo getLastOne();
}
