package com.example;


import cn.hutool.json.JSONUtil;
import com.qiniu.util.Json;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.List;

public class JedisTest {
    @Autowired
    private RedisTemplate redisTemplate;
    @Test
    void testSpringData(){
        List<String> list=new ArrayList<>();
        list.add("666");
        list.add("777");
        System.out.println(JSONUtil.parse(list).toString());
    }

}
