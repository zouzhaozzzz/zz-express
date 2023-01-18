package com.zouzhao.sys.org.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 姚超
 * @DATE: 2023-1-17
 * @DESCRIPTION:
 */
@RestController
@RequestMapping("/demo")
public class DemoController {
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

   @GetMapping("/redis")
    public void testRedis(){
        redisTemplate.opsForValue().set("kkk","123");
    }
}
