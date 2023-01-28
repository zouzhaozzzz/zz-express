package com.zouzhao.common.utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * @USER: zouzhao
 * @DATE: 2022/11/2 19:40
 * @DESCRIPTION:
 */

@Component
public class RedisManager {

    @Autowired
    private static RedisTemplate<String,Object> redisTemplate;

    public static  <T> T getValue(String key, Function<Integer,T> mapper) {
        Object o = redisTemplate.opsForValue().get(key);
        if (ObjectUtils.isEmpty(o)) {
            String[] split = key.split(":");
            T t = mapper.apply(Integer.valueOf(split[1]));
            setValue(key,t);
            return t;
        }
        return (T) o;
    }

    public static String getValue(String key) {
        Object o = redisTemplate.opsForValue().get(key);
        if (ObjectUtils.isEmpty(o)) return null;
        return (String) o;
    }


    public static void setValue(String key,Object value){
        redisTemplate.opsForValue().set(key,value,1, TimeUnit.DAYS);
    }

    public static <T> T getHashValue(String key,String hashKey, Function<Integer,T> mapper) {
        Object o = redisTemplate.opsForHash().get(key,hashKey);
        if (ObjectUtils.isEmpty(o)) {
            T t = mapper.apply(Integer.valueOf(hashKey));
            setHashValue(key,hashKey,t);
            return t;
        }
        return (T) o;
    }
    public static void setHashValue(String key,String hashKey,Object value){
        redisTemplate.opsForHash().put(key,hashKey,value);
    }
}

