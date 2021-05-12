package com.jjh.util;

import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Set;

/**
 * @program: shiro-test
 * @description
 * @author: jjh
 * @create: 2019-11-24 21:21
 **/
@Component
public class JedisUtil {

    @Resource
    private JedisPool jedisPool;

    /**
     * 获得连接
     * @return
     */
    private Jedis getResource(){
        return jedisPool.getResource();
    }

    /**
     * 存入redis
     * @param key
     * @param value
     * @return
     */
    public byte[] set(byte[] key, byte[] value) {
        Jedis jedis = getResource();
        try {
            jedis.set(key, value);
        }finally {
            jedis.close();
        }
        return value;
    }

    /**
     * 设置超时时间
     * @param key
     * @param i
     */
    public void expire(byte[] key, int i) {
        Jedis jedis = getResource();
        try {
            jedis.expire(key, i);
        }finally {
            jedis.close();
        }
    }

    /**
     * 获取key对应的value
     * @param key
     * @return
     */
    public byte[] get(byte[] key) {
        System.out.println("read redis");
//        System.out.println(Arrays.toString(key));

        Jedis jedis = getResource();
        try {
            return jedis.get(key);
        }finally {
            jedis.close();
        }
    }

    /**
     * 删除
     * @param key
     */
    public void del(byte[] key) {
        Jedis jedis = getResource();
        try {
            jedis.del(key);
        }finally {
            jedis.close();
        }
    }

    /**
     * 根据前缀取到所有key
     * @param shiro_session_prefix
     * @return
     */
    public Set<byte[]> keys(String shiro_session_prefix) {
        Jedis jedis = getResource();
        try {
            return jedis.keys((shiro_session_prefix + "*").getBytes());
        }finally {
            jedis.close();
        }
    }
}
