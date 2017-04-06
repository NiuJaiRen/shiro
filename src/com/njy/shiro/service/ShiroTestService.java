package com.njy.shiro.service;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
//@Component
//@Transactional
@Service
public class ShiroTestService {
	String ymdhms = "yyyy-MM-dd HH:mm:ss";
	@RequiresRoles({"admin"})
	public void testMethod() {
		System.out.println("shiroTest : "+new SimpleDateFormat().format(new Date()));
	}

}
