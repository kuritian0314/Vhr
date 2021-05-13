package com.jjh.test;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.SimpleAccountRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Before;
import org.junit.Test;

/**
 * @program: shiro-test
 * @description
 * @author: jjh
 * @create: 2019-10-20 15:20
 **/
public class AuthenticationTest {

    /**
     * shiro默认提供的Realm.作用是用来认证和授权
     */
    SimpleAccountRealm simpleAccountRealm = new SimpleAccountRealm();

    /**
     * 测试之前先通过Realm添加账号，用于和登录时候填写的账号进行验证登录。
     */
    @Before
    public void addUser(){
        //添加账号名称为jjh密码为123456的用户，设置其权限为admin.
        simpleAccountRealm.addAccount("jjh",
                "123456", "admin");
    }

    /**
     * 测试主体
     */
    @Test
    public void testAuthentication(){

        //1.创建Security Manager
        DefaultSecurityManager securityManager = new DefaultSecurityManager();
        securityManager.setRealm(simpleAccountRealm);

        //2.将Security Manager配置到环境中
        SecurityUtils.setSecurityManager(securityManager);

        //3.创建一个常用的账号密码主体，这里是实际登录需要进行验证的用户
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token =
                new UsernamePasswordToken("jjh", "222");

        //登录
        subject.login(token);


        //验证是否已登录
        System.out.println("isAuthentication:" + subject.isAuthenticated());

        //验证账号
        subject.checkRoles("admin");


        //登出
        subject.logout();

        //再次查询用户是否登录
        System.out.println("isAuthentication:" + subject.isAuthenticated());


    }
}
