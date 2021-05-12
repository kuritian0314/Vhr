package com.jjh.filter;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.AuthenticationFilter;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;

/**
 * @program: shiro-test
 * @description
 * @author: jjh
 * @create: 2019-10-27 21:57
 **/
public class RolesOrFilter extends AuthorizationFilter {
    @Override
    protected boolean isAccessAllowed(
            javax.servlet.ServletRequest servletRequest,
            javax.servlet.ServletResponse servletResponse,
            Object o) throws Exception {
        //当前请求主体
        Subject subject = getSubject(servletRequest, servletResponse);
        //取到xml中配置的角色
        String[] roles = (String[]) o;
        if (roles == null || roles.length == 0){
            //未配置，直接验证通过
            return true;
        }
        for (String role : roles){
            //只要包含其中一个角色，就通过
            if (subject.hasRole(role)){
                return true;
            }
        }
        return false;
    }
}
