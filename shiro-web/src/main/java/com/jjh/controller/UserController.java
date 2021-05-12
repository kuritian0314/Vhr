package com.jjh.controller;

import com.jjh.vo.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserController {
	
	@RequestMapping("/test.do")
	@ResponseBody
	public String test() {
		return "test";
	}
	
	@RequestMapping(
			value="/subLogin", 
			method=RequestMethod.POST,
			produces="application/json;charset=utf-8")
	@ResponseBody
	public String subLogin(User user) {
		String msg = "";
		Subject subject = SecurityUtils.getSubject();

		UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(), user.getPassword());

		try {
			token.setRememberMe(user.isRememberMe());
			subject.login(token);
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
		
		// 判断是否具有管理员身份(角色)
		if (subject.hasRole("admin")) {
			msg += "有admin角色";
		}else {
		    msg += "无admin角色";
        }

        //判断是否拥有权限
		if (subject.isPermitted("user:update")){
		    msg += "拥有更新权限";
        }else {
		    msg += "无更新权限";
        }
		return msg;
	}
	
	@RequiresRoles("admi2n")
	@RequestMapping(value="/testRole", method=RequestMethod.GET)
	@ResponseBody
	public String testRole() {
		return "test role success";
	}
	


	@RequiresPermissions("user:update")
	@RequestMapping(value="/testpermission", method=RequestMethod.GET)
	@ResponseBody
	public String testRoles1() {
		return "test permission success";
	}


    @RequestMapping(value="/testRole1", method=RequestMethod.GET)
    @ResponseBody
    public String testRole1() {
        return "test role1 success";
    }

    @RequestMapping(value="/testRole2", method=RequestMethod.GET)
    @ResponseBody
    public String testRole2() {
        return "test roles2 success";
    }

	@RequestMapping(value="/testRole3", method=RequestMethod.GET)
	@ResponseBody
	public String testRole3() {
		return "test roles3 success";
	}

    @RequestMapping(value="/testPerm1", method=RequestMethod.GET)
    @ResponseBody
    public String testPerm1() {
        return "test perm1 success";
    }

    @RequestMapping(value="/testPerm2", method=RequestMethod.GET)
    @ResponseBody
    public String testPerm2() {
        return "test perm2 success";
    }

}
