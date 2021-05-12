package com.jjh.cache;

import com.jjh.util.JedisUtil;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Set;

/**
 * @program: shiro-test
 * @description
 * @author: jjh
 * @create: 2019-12-02 20:14
 **/
@Component
public class RedisCache implements Cache {

    @Resource
    private JedisUtil jedisUtil;

    private final String CACHE_PERFIX = "jjh-cache";

    /**
     * 返回带固定前缀的key
     * @param key
     * @return key 的byte数组
     */
    private byte[] getKey(Object key){
        if (key instanceof String){
            return (CACHE_PERFIX + key).getBytes();
        }
        return SerializationUtils.serialize(key);
    }


    /**
     * 根据key返回值
     * @param k
     * @return
     * @throws CacheException
     */
    @Override
    public Object get(Object k) throws CacheException {
        System.out.println("从自定义Cache中读取");
        byte[] value = jedisUtil.get(getKey(k));
        if (value != null){
            return SerializationUtils.deserialize(value);
        }
        return null;
    }

    @Override
    public Object put(Object k, Object v) throws CacheException {
        byte[] key = getKey(k);
        byte[] value = SerializationUtils.serialize(v);
        jedisUtil.set(key,value);
        jedisUtil.expire(key, 600);
        return v;
    }


    @Override
    public Object remove(Object k) throws CacheException {
        byte[] key = getKey(k);
        byte[] value = jedisUtil.get(key);
        jedisUtil.del(key);
        return value;
    }

    /**
     * 不需要重写该方法，有可能清理掉其它不属于此的数据
     * @throws CacheException
     */
    @Override
    public void clear() throws CacheException {

    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public Set keys() {
        return jedisUtil.keys(CACHE_PERFIX);
    }

    @Override
    public Collection values() {
        return null;
    }
}
