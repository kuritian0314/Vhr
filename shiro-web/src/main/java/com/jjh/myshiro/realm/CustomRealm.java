package com.jjh.myshiro.realm;

import com.jjh.dao.UserDao;
import com.jjh.vo.User;
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

import javax.annotation.Resource;
import java.util.*;

/**
 * @program: shiro-test
 * @description
 * @author: jjh
 * @create: 2019-10-20 17:14
 **/
public class CustomRealm extends AuthorizingRealm {

    @Resource
    private UserDao userDaoImpl;

//    Map<String,String> userMap = new HashMap<>(16);

    {
        super.setName("customRealm");

//        userMap.put("jjh", "123456");
        //md5值
//        userMap.put("jjh", "e10adc3949ba59abbe56e057f20f883e");
        //md5加盐jjh值
//        userMap.put("jjh", "35ce358d2a203cbaa486966ec9d3871a");
    }

    /**
     * 实现授权
     *
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //取到用户信息
        String userName = (String) principalCollection.getPrimaryPrincipal();
        //取得对应的角色和权限
        Set<String> roles = getRolesByUserName(userName);
        Set<String> permissions = getPermissionsByUserName(userName);
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        //返回数据
        simpleAuthorizationInfo.addRoles(roles);
        simpleAuthorizationInfo.addStringPermissions(permissions);
        return simpleAuthorizationInfo;
    }


    /**
     * 实现认证功能
     *
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String userName = (String) authenticationToken.getPrincipal();
        String password = getPasswordByUserName(userName);
        if (password == null){
            return null;
        }
        SimpleAuthenticationInfo simpleAuthorizationInfo =
                new SimpleAuthenticationInfo(userName, password, "customRealm");
//        simpleAuthorizationInfo.setCredentialsSalt(ByteSource.Util.bytes(userName));
        //设置盐值为jjh
        simpleAuthorizationInfo.setCredentialsSalt(ByteSource.Util.bytes("jjh"));
        return simpleAuthorizationInfo;
    }

    /**
     * 根据用户名返回密码
     * @param userName
     * @return
     */
    private String getPasswordByUserName(String userName) {
        User user = userDaoImpl.getUserByUserName(userName);
        if (user != null) {
            return user.getPassword();
        }
        return null;
    }


    /**
     * 根据用户返回权限
     * @param userName
     * @return
     */
    private Set<String> getPermissionsByUserName(String userName) {
        List<String> list = userDaoImpl.queryPermissionsByUserName(userName);
        HashSet permissions = new HashSet(list);
        return permissions;
    }

    /**
     * 根据用户返回角色
     * @param userName
     * @return
     */
    private Set<String> getRolesByUserName(String userName) {
        System.out.println("从数据库中进行读取数据");
        List<String> list = userDaoImpl.queryRolesByUserName(userName);
        HashSet roles = new HashSet(list);
        return roles;
    }

    public static void main(String[] args) {
        Md5Hash md5Hash = new Md5Hash("ad123", "jjh");
        System.out.println(md5Hash.toString());

    }

}
