package com.example;

import com.Travel.server.UserService;
import com.Travel.util.RedisIdWorker;
import com.Travel.vo.Result;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
class TravelApplicationTests {
    @Autowired
     private RedisIdWorker redisIdWorker;
    @Autowired
    private UserService userService;

    @Test
    void contextLoads() {
        List<String> list=new ArrayList<>();
        list.add("穷游");
        Result result = userService.searchUsersByTags(list);
        System.out.println(result.getData());
    }

}
