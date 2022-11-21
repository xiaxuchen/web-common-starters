package org.originit.example;

import org.junit.jupiter.api.Test;
import org.originit.redismq.domain.User;
import org.originit.redismq.template.RedisMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
public class TestExample {

    @Autowired
    RedisMQTemplate redisMQTemplate;


    @Autowired
    RedisTemplate<String,String> redisTemplate;

    @Test
    void testRedis() throws InterruptedException {
        while (true) {
            Thread.sleep(100);
            redisMQTemplate.convertAndSend(RedisMQTemplate.DEFAULT_LOG_DESTINATION,new User("xas",12));
        }
    }
}
