package com.imooc.security.core.authentication.mobile;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public class SmsCodeAuthenticationProvider implements AuthenticationProvider {

	private UserDetailsService userDetailsService;

    /**
     * 进行身份认证的逻辑。
     *      验证短信验证码的逻辑写在SmsCodeAuthenticationFilter之前，而不是写在SmsCodeAuthenticationProvider。
     */
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		SmsCodeAuthenticationToken authenticationToken = (SmsCodeAuthenticationToken) authentication;

		/* userDetailsService：根据手机号或用户名，获取用户信息
		        此处需要我们在UserDetailsService实现类里面写  根据手机号查询用户信息。
		 */
		UserDetails user = userDetailsService.loadUserByUsername((String) authenticationToken.getPrincipal());
		if (user == null) {
			throw new InternalAuthenticationServiceException("无法获取用户信息");
		}

		// 把认证成功的用户信息设置到AuthenticationToken实体类中。
		SmsCodeAuthenticationToken authenticationResult = new SmsCodeAuthenticationToken(user, user.getAuthorities());
		authenticationResult.setDetails(authenticationToken.getDetails());
		return authenticationResult;
	}

    /**
     * AuthenticationManager调取该方法，判断当前provider是否支持这个AuthenticationToken实现类。
     */
	@Override
	public boolean supports(Class<?> authentication) {
		return SmsCodeAuthenticationToken.class.isAssignableFrom(authentication);
	}

	public UserDetailsService getUserDetailsService() {
		return userDetailsService;
	}

	public void setUserDetailsService(UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}
}
