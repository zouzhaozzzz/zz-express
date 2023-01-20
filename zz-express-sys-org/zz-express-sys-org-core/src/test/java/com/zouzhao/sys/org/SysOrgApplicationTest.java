package com.zouzhao.sys.org;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author 姚超
 * @date: 2023-1-17
 */
@SpringBootTest
public class SysOrgApplicationTest {

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Test
    public void testRedis(){
        redisTemplate.opsForValue().set("kkk","1234");
    }
}
