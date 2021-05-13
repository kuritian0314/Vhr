package com.jjh.test;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.SimpleAccountRealm;
import org.apache.shiro.realm.text.IniRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Before;
import org.junit.Test;

/**
 * @program: shiro-test
 * @description
 * @author: jjh
 * @create: 2019-10-20 15:20
 **/
public class IniRealmTest {


    @Test
    public void testAuthentication(){

        IniRealm iniRealm = new IniRealm("classpath:user.ini");

        //1.创建Security Manager
        DefaultSecurityManager securityManager = new DefaultSecurityManager();
        securityManager.setRealm(iniRealm);

        //配置到环境中
        SecurityUtils.setSecurityManager(securityManager);

        //获取主体
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token =
                new UsernamePasswordToken("jjh", "123456");
        subject.login(token);


        //验证登录
        System.out.println("isAuthentication:" + subject.isAuthenticated());

        subject.checkRoles("admin");

        subject.checkPermissions("user:delete", "user:update");


    }
}
