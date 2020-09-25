package com.imooc.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.social.security.SocialUser;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.social.security.SocialUserDetailsService;
import org.springframework.stereotype.Component;

/**
 * 这个类的作用：根据userId找到完整的用户信息。
 * 要继承SocialUserDetailsService接口。
 * spring social 根据providerId、providerUserId找到userId，根据userId找到完整的用户信息，
 * 将完整的用户信息放到spring security中，spring security就认为用户登录成功了。
 */
@Component
public class MyUserDetailsService implements UserDetailsService, SocialUserDetailsService {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	/** 根据用户名查询用户信息。
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		logger.info("表单登录用户名:" + username);
		return buildUser(username);
	}

	/**
	 * 实现SocialUserDetailsService，重写loadUserByUserId方法。根据userId查询用户信息。
	 * 父子关系：  SocialUser 继承 SocialUserDetails 继承  UserDetails 。
	 */
	@Override
	public SocialUserDetails loadUserByUserId(String userId) throws UsernameNotFoundException {
		// 如何根据userId查询到用户信息，每个项目的业务逻辑不同，此处不演示。
		logger.info("社交登录用户Id:" + userId);
		return buildUser(userId);
	}

	private SocialUserDetails buildUser(String userId) {
		String password = passwordEncoder.encode("123456");
		logger.info("数据库密码是:"+password);
		return new SocialUser(userId, password, true, true, true, true,
				AuthorityUtils.commaSeparatedStringToAuthorityList("admin,ROLE_USER,ROLE_ADMIN"));
	}
}
