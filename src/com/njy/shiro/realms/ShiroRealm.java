package com.njy.shiro.realms;

import java.util.HashSet;
import java.util.Set;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;


public class ShiroRealm extends AuthorizingRealm  {

	/**
	 * 认证回调函数,登录时调用.
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) 
			throws AuthenticationException {
		System.out.println("doGetAuthenticationInfo:"+token);
		//1.AuthenticationToken转成UsernamePasswordToken对象
		UsernamePasswordToken UsernamePasswordToken = (org.apache.shiro.authc.UsernamePasswordToken) token;
		//2.从UsernamePasswordToken对象获取username
		String username = UsernamePasswordToken.getUsername();
		//3.数据库查询username
		System.out.println("从数据库获取username："+username+"所对应的用户信息。");
		//4.如用户不存在，抛出异常
		if("unknown".equals(username)){
			throw new UnknownAccountException("用户不存在");
		}
		//5.根据用户信息的情况，决定是否需要抛出其他的AuthenticationException
		if("monster".equals(username)){
			throw new LockedAccountException("用户被锁定");
		}
		//6.根据用户情况，来构建AuthenticationInfo 对象并返回。通常使用的实现
		//1） principal:认证的实体信息
		Object principal = username;
		//2) credentials 密码
		Object credentials = null;
		if("admin".equals(username)){
			credentials = "038bdaf98f2037b31f1e75b5b4c9b26e";
		}
		if("user".equals(username)){
			credentials = "098d2c478e9c11555ce2823231e02ec1";
		}
		//Object credentials = "fc1709d0a95a6be30bc5926fdb7f22f4";
		//3) realmName 当前realm对象的name。调用父类的 getName() 方法即可
		String realmName = getName();
		//4) 盐值
		ByteSource credentialsSalt = ByteSource.Util.bytes(username);
		SimpleAuthenticationInfo info = null;//new SimpleAuthenticationInfo(principal,credentials,realmName);
		info = new SimpleAuthenticationInfo(principal, credentials, credentialsSalt, realmName);
		return info;
	}
	
	/**
	 * 授权需要实现的方法
	 * @param principals
	 * @return
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(
			PrincipalCollection principals) {
		//1.获取登录用户的信息
		Object principal = principals.getPrimaryPrincipal();
		//2.利用登录用户的信息来获取当前用户的角色或权限（可能查询数据库）
		Set<String> roles = new HashSet<>();
		if("admin".equals(principal)){
			roles.add("admin");
		};
		if("user".equals(principal)){
			roles.add("user");
		};
		//3.创建 SimpleAuthorizationInfo，设置roles 属性
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo(roles);
		//4.返回SimpleAuthorizationInfo 对象
		return info;
	}

	public static void main(String[] args) {
		String hash = "MD5";
		Object credentials = "123456";
		Object salt = null;
		int hashcishu = 1024;
		ByteSource credentialsSalt = ByteSource.Util.bytes("user");
		Object result = new SimpleHash(hash,credentials,credentialsSalt,hashcishu);
		System.out.println(result);
	}

}
