package com.imooc.security.core.authentication.mobile;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.Assert;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 摘抄UsernamePasswordAuthenticationFilter源码并对其修改
 */
public class SmsCodeAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

	// 请求中携带参数的名称
	private String mobileParameter = "mobile";

	// 是否只过滤post请求
	private boolean postOnly = true;

	/**
	 * 指定这个过滤器要处理的请求是什么。就是登陆表单提交的URL。
	 */
	public SmsCodeAuthenticationFilter() {
	    super(new AntPathRequestMatcher("/authentication/mobile", "POST"));
	}


	/**
	 * 拦截   属于这个过滤器过滤范围 的http请求。认证流程。
	 */
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		if (postOnly && !request.getMethod().equals("POST")) {
			throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
		}

		// 获取手机号
		String mobile = obtainMobile(request);
		if (mobile == null) {
			mobile = "";
		}
		mobile = mobile.trim();

		// 实例化AuthenticationToken
		SmsCodeAuthenticationToken authenticationToken = new SmsCodeAuthenticationToken(mobile);

		// 把请求的信息也设置到AuthenticationToken
		setDetails(request, authenticationToken);

		// 调取AuthenticationManager,
		return this.getAuthenticationManager().authenticate(authenticationToken);
	}

	/**
	 * 获取手机号
	 */
	protected String obtainMobile(HttpServletRequest request) {
		return request.getParameter(mobileParameter);
	}


	protected void setDetails(HttpServletRequest request, SmsCodeAuthenticationToken authRequest) {
		authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
	}

	public void setMobileParameter(String usernameParameter) {
		Assert.hasText(usernameParameter, "Username parameter must not be empty or null");
		this.mobileParameter = usernameParameter;
	}

	public void setPostOnly(boolean postOnly) {
		this.postOnly = postOnly;
	}

	public final String getMobileParameter() {
		return mobileParameter;
	}
}
