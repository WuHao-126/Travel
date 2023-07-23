package com.example;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
public class JedisTest {
    @Autowired
    private RedisTemplate redisTemplate;
    @Test
    void testSpringData(){
        System.out.println(redisTemplate);
        redisTemplate.opsForValue().set("name","huge");
        Object wuhao = redisTemplate.opsForValue().get("wuhao");
        System.out.println(wuhao);
    }

}
