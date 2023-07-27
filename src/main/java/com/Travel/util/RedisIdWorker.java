package com.Travel.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RedisIdWorker {
    @Autowired
    private  StringRedisTemplate stringRedisTemplate;
    private static final long START_DATETIME=1672531200l;
    public  long nextId(String key){
        LocalDateTime now = LocalDateTime.now();
        long nowDateTime = now.toEpochSecond(ZoneOffset.UTC);
        long timestamp=nowDateTime-START_DATETIME;
        String format = now.format(DateTimeFormatter.ofPattern("yyyy:MM:dd"));
        Long increment = stringRedisTemplate.opsForValue().increment("icr" + key + ":"+format);
        return timestamp << 32 | increment;
    }

    public static void main(String[] args) {

    }
}
