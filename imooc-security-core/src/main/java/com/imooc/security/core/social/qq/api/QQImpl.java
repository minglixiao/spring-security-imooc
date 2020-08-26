package com.imooc.security.core.social.qq.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.oauth2.AbstractOAuth2ApiBinding;
import org.springframework.social.oauth2.TokenStrategy;

/**
 * 实现 “spring social逻辑流程图” 第6步：根据令牌（accessToken）获取用户信息。
 * 继承AbstractOAuth2ApiBinding。
 * 这个类需要是多例，不能是单例，因为这个类会存每次流程的属性。
 */
public class QQImpl extends AbstractOAuth2ApiBinding implements QQ {

	// 根据accessToken获取用户openid的 URL
	private static final String URL_GET_OPENID = "https://graph.qq.com/oauth2.0/me?access_token=%s";

	// 根据accessToken获取用户信息的 URL。父类会自动给我们http请求添加accessToken参数
	private static final String URL_GET_USERINFO = "https://graph.qq.com/user/get_user_info?oauth_consumer_key=%s&openid=%s";

	// 服务提供商给第三方应用的唯一标识符
	private String appId;

	// 用户在服务提供商的userid
	private String openId;

	private ObjectMapper objectMapper = new ObjectMapper();

	private Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 初始化成员变量accessToken、appId、openId
	 * @param accessToken : 从服务提供商获取到的令牌
	 * @param appId :
	 */
	public QQImpl(String accessToken, String appId) {
		/* TokenStrategy.AUTHORIZATION_HEADER :   在RestTemplate发送http请求时，将参数accessToken放到http请求头中。默认值。
		   TokenStrategy.ACCESS_TOKEN_PARAMETER : 在RestTemplate发送http请求时，将参数accessToken放到http请求参数中。
		 */
		// 初始化成员变量accessToken。
		super(accessToken, TokenStrategy.ACCESS_TOKEN_PARAMETER);
		this.appId = appId;

		// 将字符串中%s，依次替换为后面的参数。
		String url = String.format(URL_GET_OPENID, accessToken);
		/* getRestTemplate() : 是从父类获取RestTemplate对象，RestTemplate是一个发送http请求的工具类。
		   getForObject() : 发送一个http请求，并将返回值类型 转换为第二个参数的类型。
		 */
		String result = getRestTemplate().getForObject(url, String.class);
		logger.info("QQ第三方登录----根据accessToken获取openid----响应内容:{}", result);
		// System.out.println(result);
		this.openId = StringUtils.substringBetween(result, "\"openid\":\"", "\"}");
	}

	/**
	 * 根据令牌向服务提供商发送请求，获取用户信息。
	 * 不同服务提供商返回的用户信息的属性是不同的，返回值QQUserInfo就是QQ返回的用户信息的属性。
	 */
	@Override
	public QQUserInfo getUserInfo() {
		String url = String.format(URL_GET_USERINFO, appId, openId);
		// 父类会自动给我们http请求添加accessToken参数
		String result = getRestTemplate().getForObject(url, String.class);
		logger.info("QQ第三方登录----根据accessToken获取用户信息----响应内容:{}", result);
		QQUserInfo userInfo = null;
		try {
			// objectMapper.readValue() : 将json格式的字符串转换为java对象
			userInfo = objectMapper.readValue(result, QQUserInfo.class);
			userInfo.setOpenId(openId);
			return userInfo;
		} catch (Exception e) {
			throw new RuntimeException("获取用户信息失败", e);
		}
	}
}
