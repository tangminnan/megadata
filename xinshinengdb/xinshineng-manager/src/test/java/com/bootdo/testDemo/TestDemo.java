package com.bootdo.testDemo;

import com.xinshineng.common.utils.MD5Utils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController()
@RunWith(SpringRunner.class)
@SpringBootTest(classes = com.xinshineng.BootdoApplication.class)

public class TestDemo {


    @Test
    public void testpwd(){
     /* String  password = MD5Utils.encrypt("huantaizhongxin", "123456");
        System.out.println(password);*/
        System.out.println(199/200);
    }
}
