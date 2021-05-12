package com.jjh.session;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.session.mgt.WebSessionKey;
import org.springframework.stereotype.Component;

import javax.servlet.ServletRequest;
import java.io.Serializable;

/**
 * @program: shiro-test
 * @description
 * @author: jjh
 * @create: 2019-11-24 23:33
 **/
@Component
public class CustomSessionManager extends DefaultWebSessionManager {
    /**
     * 重写方法，优先从request对象中读取session，减轻redis压力
     * @param sessionKey
     * @return
     * @throws UnknownSessionException
     */
    @Override
    protected Session retrieveSession(SessionKey sessionKey) throws UnknownSessionException {
        //取到sessionId
        Serializable sessionId = getSessionId(sessionKey);
        ServletRequest request = null;
        //获取request0
        if (sessionKey instanceof WebSessionKey){
            request = ((WebSessionKey) sessionKey).getServletRequest();
        }
        //获取session
        Session session = null;
        if (request != null && sessionId != null){
            session = (Session) request.getAttribute(sessionId.toString());
            if (session != null){
                return  session;
            }
        }
        session = super.retrieveSession(sessionKey);
        if (request != null && sessionId != null) {
            request.setAttribute(sessionId.toString(), session);
        }
        return session;
    }

}
