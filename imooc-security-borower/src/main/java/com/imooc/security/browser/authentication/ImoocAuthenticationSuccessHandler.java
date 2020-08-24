package com.imooc.security.browser.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imooc.security.core.properties.LoginResponseType;
import com.imooc.security.core.properties.SecurityProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 实现AuthenticationSuccessHandler接口。SavedRequestAwareAuthenticationSuccessHandler
 * AuthenticationSuccessHandler的实现类，是spring  security默认使用的登录成功处理。
 */
@Component("imoocAuthenticationSuccessHandler")
public class ImoocAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private SecurityProperties securityProperties;

	/**
	 * 参数Authentication作用: 封装我们的认证信息。Authentication是个接口，根据登录方式的不同，实现类是不同的。
	 * 我们此处登录方式：用户名密码登陆。以后还会讲：第三方登录，微信登录，QQ登录。
	 */
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
										Authentication authentication) throws IOException, ServletException {
		logger.info("登录成功");
		// 推荐的方式：登录页面表单提交之后，后端返回一个JSON数据
		if (LoginResponseType.JSON.equals(securityProperties.getBrowser().getLoginType())) {
			response.setContentType("application/json;charset=UTF-8");
			// 将javaBean转化为JSON格式的字符串
			String str = objectMapper.writeValueAsString(authentication);
			// 返回一个JSON
			response.getWriter().write(str);
		} else {
			// 默认的方式：重定向到引发跳转登录的那个RESTful接口或页面上
			super.onAuthenticationSuccess(request, response, authentication);
		}
	}
}
