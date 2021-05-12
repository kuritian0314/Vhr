package com.jjh.dao;

import com.jjh.vo.User;

import java.util.List;

/**
 * @program: shiro-test
 * @description
 * @author: jjh
 * @create: 2019-10-22 21:17
 **/
public interface UserDao {
    /**
     * 根据用户名取得用户信息
     * @param userName
     * @return
     */
    User getUserByUserName(String userName);

    /**
     * 根据用户名查询用户角色信息
     * @param userName
     * @return
     */
    List<String> queryRolesByUserName(String userName);

    /**
     * 根据用户名查询权限
     * @param userName
     * @return
     */
    List<String> queryPermissionsByUserName(String userName);
}
