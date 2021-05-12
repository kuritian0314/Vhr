package com.jjh.session;

import com.jjh.util.JedisUtil;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.SerializationUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @program: shiro-test
 * @description
 * @author: jjh
 * @create: 2019-11-24 21:20
 **/
@Component
public class RedisSessionDao extends AbstractSessionDAO {

    @Resource
    private JedisUtil jedisUtil;

    /**
     * 自定义生成sessionId时加上前缀。
     */
    private final String SHIRO_SESSION_PREFIX = "jjh-session:";

    /**
     * 定义通过字符串key返回 byte数组的key
     * 作用是避免在不同平台之间传递，由于编码等原因可能带来的问题。
     *
     * @param key
     * @return
     */
    private byte[] getKey(String key) {
        return (SHIRO_SESSION_PREFIX + key).getBytes();
    }

    /**
     * 保存session
     *
     * @param session
     */
    private void saveSession(Session session) {
        if (session != null && session.getId() != null) {
            //生成session键值对
            byte[] key = getKey(session.getId().toString());
            byte[] value = SerializationUtils.serialize(session);
            //设置到redis 超时为600秒。
            jedisUtil.set(key, value);
            jedisUtil.expire(key, 600);
        }
    }

    /**
     * 创建session,返回sessionId
     *
     * @param session
     * @return
     */
    @Override
    protected Serializable doCreate(Session session) {
        //根据session生成sessionId
        Serializable sessionId = generateSessionId(session);
        //将sessionId和session进行捆绑
        assignSessionId(session, sessionId);
        //保存session
        saveSession(session);
        return sessionId;
    }

    /**
     * 根据sessionId返回session
     *
     * @param sessionId
     * @return
     */
    @Override
    protected Session doReadSession(Serializable sessionId) {
        if (StringUtils.isEmpty(sessionId)) {
            return null;
        }
        byte[] key = getKey(sessionId.toString());
        byte[] value = jedisUtil.get(key);
        return (Session) SerializationUtils.deserialize(value);
    }

    @Override
    public void update(Session session) throws UnknownSessionException {
        saveSession(session);
    }

    @Override
    public void delete(Session session) {
        if (session == null || session.getId() == null){
            return;
        }
        byte[] key = SerializationUtils.serialize(session.getId().toString());
        jedisUtil.del(key);
    }

    /**
     * 获取所有session
     * @return
     */
    @Override
    public Collection<Session> getActiveSessions() {
        //前缀的作用得以体现
        Set<byte[]> keys = jedisUtil.keys(SHIRO_SESSION_PREFIX);
        Set<Session> sessions = new HashSet<Session>();
        if (CollectionUtils.isEmpty(keys)){
            return sessions;
        }
        for (byte[] key : keys){
            Session session = (Session) SerializationUtils.deserialize(jedisUtil.get(key));
            sessions.add(session);
        }
        return sessions;
    }
}
