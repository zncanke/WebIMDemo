package com.will.imlearntest.bo;

import com.will.imlearntest.dao.HelloWorldDao;
import com.will.imlearntest.po.HelloWorldPo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by willl on 5/16/16.
 */
@Service
public class HelloWorldBo {

    @Autowired
    private HelloWorldDao helloWorldDao;

    public String getHello() {
        HelloWorldPo lastOne = helloWorldDao.getLastOne();
        return lastOne.getHello();
    }
}
