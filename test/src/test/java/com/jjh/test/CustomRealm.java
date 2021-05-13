package com.jjh.test;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @program: shiro-test
 * @description
 * @author: jjh
 * @create: 2019-10-20 17:14
 **/
public class CustomRealm extends AuthorizingRealm {

    Map<String,String> userMap = new HashMap<>(16);

    //构造块
    {
//        userMap.put("jjh", "123456");
        //md5加盐 35ce358d2a203cbaa486966ec9d3871a是jjh的md5值
        userMap.put("jjh", "35ce358d2a203cbaa486966ec9d3871a");
        //取名字
        super.setName("customRealm");
    }

    /**
     * 重写方法实现认证功能
     *
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //取得用户信息
        String userName = (String) authenticationToken.getPrincipal();
        //取用户的密码，这里我是直接用的构造块中的map取得加盐后的密码。
        //如果是从数据库中取，这里应该取到当前用户名对应存在数据库的密码(不区分是否加盐)
        String password = getPasswordByUserName(userName);
        if (password == null){
            return null;
        }
        //这里的业务逻辑是，根据用户信息，设置认证
        SimpleAuthenticationInfo simpleAuthorizationInfo =
                new SimpleAuthenticationInfo(userName, password, "customRealm");
        //如果数据库存储的是加盐后的密码，设置盐值用于实际登录验证
        simpleAuthorizationInfo.setCredentialsSalt(ByteSource.Util.bytes("jjh"));
        //返回进行校验
        return simpleAuthorizationInfo;
    }

    /**
     * 重写方法实现授权功能
     *
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //取到用户信息
        String userName = (String) principalCollection.getPrimaryPrincipal();
        //实际业务是根据用户取得对应的角色，我方法里直接返回了admin角色
        Set<String> roles = getRolesByUserName(userName);
        //根据用户取权限，直接返回了user:delete和user:update
        Set<String> permissions = getPermissionsByUserName(userName);
        //创建返回对象
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        //添加角色和权限数据
        simpleAuthorizationInfo.addRoles(roles);
        simpleAuthorizationInfo.addStringPermissions(permissions);
        //返回
        return simpleAuthorizationInfo;
    }


    /**
     * 根据用户名返回密码
     * @param userName
     * @return
     */
    private String getPasswordByUserName(String userName) {
        return userMap.get(userName);
    }


    /**
     * 根据用户返回权限
     * @param userName
     * @return
     */
    private Set<String> getPermissionsByUserName(String userName) {
        HashSet permissions = new HashSet();
        permissions.add("user:delete");
        permissions.add("user:update");
        return permissions;
    }

    /**
     * 根据用户返回角色
     * @param userName
     * @return
     */
    private Set<String> getRolesByUserName(String userName) {
        HashSet roles = new HashSet();
        roles.add("admin");
        return roles;
    }

    public static void main(String[] args) {
        //请忽略这个，单纯计算一下jjh的md5值
        Md5Hash md5Hash = new Md5Hash("123456", "jjh");
        System.out.println(md5Hash.toString());

    }

}
