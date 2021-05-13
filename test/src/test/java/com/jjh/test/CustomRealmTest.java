package com.jjh.test;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

/**
 * @program: shiro-test
 * @description
 * @author: jjh
 * @create: 2019-10-20 15:20
 **/
public class CustomRealmTest {

    @Test
    public void testAuthentication(){

        CustomRealm customRealm = new CustomRealm();

        //1.创建Security Manager
        DefaultSecurityManager securityManager = new DefaultSecurityManager();
        securityManager.setRealm(customRealm);

        //加密方式
        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher();
        //使用md5加盐
        matcher.setHashAlgorithmName("md5");
        //加盐次数
        matcher.setHashIterations(1);
        customRealm.setCredentialsMatcher(matcher);

        //配置到环境中
        SecurityUtils.setSecurityManager(securityManager);

        //获取主体
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token =
                new UsernamePasswordToken("jjh", "123456");
        //这里login的时候，就会去调用Realm方法返回的AuthenticationInfo来进行认证。
        subject.login(token);


        //验证登录
        System.out.println("isAuthentication:" + subject.isAuthenticated());

        subject.checkRoles("admin");

        subject.checkPermissions("user:delete", "user:update");


    }
}
