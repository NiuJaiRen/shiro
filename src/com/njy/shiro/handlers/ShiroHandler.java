package com.njy.shiro.handlers;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.njy.shiro.service.ShiroTestService;

@Controller
@RequestMapping("/shiro")
public class ShiroHandler {
	@Autowired
	private ShiroTestService shiroTestService;
	
	@RequestMapping("/testShiroAnnotation")
	public String testShiroAnnotation() {
		this.shiroTestService.testMethod();
		return "redirect:/list.jsp";
	}
	
	@RequestMapping("/login")
	public String login(@RequestParam("username") String username,
			@RequestParam("password") String password,@RequestParam("remeberMe") String remeberMe){
		 //获取当前的subject,调用SecurityUtils.getSubject()
        Subject currentUser = SecurityUtils.getSubject();
        // 测试当前用户是否被认证即是否已经登录
        if (!currentUser.isAuthenticated()) {
        	//把用户名和密码封装为UsernamePasswordToken对象
            UsernamePasswordToken token = new UsernamePasswordToken(username, password);
            //remenberme
            if(remeberMe != null && "yes".equals(remeberMe)){
            	 System.out.println(remeberMe);
            	 token.setRememberMe(true);
            }
           
            try {
            	//执行登陆
                currentUser.login(token);
            }
            // ... catch more exceptions here (maybe custom ones specific to your application?
            //所有的认证异常的父类
            catch (AuthenticationException ae) {
                //unexpected condition?  error?
            	System.out.println("登录失败："+ae.getMessage());
            }
        }
        //return "list";
		 return "redirect:/list.jsp";
	}
	
}
