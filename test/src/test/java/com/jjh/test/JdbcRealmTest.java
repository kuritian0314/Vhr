package com.jjh.test;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.realm.text.IniRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

/**
 * @program: shiro-test
 * @description
 * @author: jjh
 * @create: 2019-10-20 15:20
 **/
public class JdbcRealmTest {


    DruidDataSource dataSource = new DruidDataSource();

    {
        dataSource.setUrl("jdbc:mysql://localhost:3306/shiro_test");
        dataSource.setUsername("root");
        dataSource.setPassword("123456");
    }


    @Test
    public void testAuthentication(){

        JdbcRealm jdbcRealm = new JdbcRealm();
        jdbcRealm.setDataSource(dataSource);
        /**
         *在授权期间启用权限查找。默认值为“false”-这意味着只有角色
         *与用户关联。将此设置为true，以便查找角色<b>和<b>权限。
         *
         *@param permissionsLookupEnabled如果在授权期间应查找权限，则为true；如果仅在授权期间应查找权限，则为false
         *应该查找角色。
         */
        jdbcRealm.setPermissionsLookupEnabled(true);

        //1.创建Security Manager
        DefaultSecurityManager securityManager = new DefaultSecurityManager();
        securityManager.setRealm(jdbcRealm);

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

        //setPermissionsLookupEnabled(false)的话这里用户是校验不了权限的，需要通过角色
        subject.checkPermissions("user:delete", "user:update");


    }
}
