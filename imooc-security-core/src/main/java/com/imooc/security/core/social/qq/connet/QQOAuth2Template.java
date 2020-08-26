package com.imooc.security.core.social.qq.connet;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2Template;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import java.nio.charset.Charset;

/**
 * 自定义QQ登录的OAuth2Template
 */
public class QQOAuth2Template extends OAuth2Template {
	
	private Logger logger = LoggerFactory.getLogger(getClass());

	public QQOAuth2Template(String clientId, String clientSecret, String authorizeUrl, String accessTokenUrl) {
		super(clientId, clientSecret, authorizeUrl, accessTokenUrl);
		// 发送请求时带上client_id和client_secret
		setUseParametersForClientAuthentication(true);
	}

	/**
	 * 覆盖postForAccessGrant方法。第5步，认证服务器返回令牌，我们针对QQ的响应结果，自定义处理逻辑，封装AccessGrant对象。
	 */
	@Override
	protected AccessGrant postForAccessGrant(String accessTokenUrl, MultiValueMap<String, String> parameters) {
		// postForObject：发送post请求。根据授权码获取申请令牌
		String responseStr = getRestTemplate().postForObject(accessTokenUrl, parameters, String.class);
		logger.info("获取accessToke的响应："+responseStr);
		String[] items = StringUtils.splitByWholeSeparatorPreserveAllTokens(responseStr, "&");
		// accessToken : 令牌
		String accessToken = StringUtils.substringAfterLast(items[0], "=");
		Long expiresIn = new Long(StringUtils.substringAfterLast(items[1], "="));
		// refreshToken : 用来刷新令牌的
		String refreshToken = StringUtils.substringAfterLast(items[2], "=");
		// AccessGrant 是对 令牌 的封装
		return new AccessGrant(accessToken, null, refreshToken, expiresIn);
	}


	/* spring social逻辑流程图第5步：返回令牌。因为QQ响应回来的内容是text/html类型类型的。默认的RestTemplate不支持。
	   所以我们需要给RestTemplate添加一个新的Converters。StringHttpMessageConverter能够处理text/html类型的响应内容
	*/
	@Override
	protected RestTemplate createRestTemplate() {
		RestTemplate restTemplate = super.createRestTemplate();
		restTemplate.getMessageConverters().add(new StringHttpMessageConverter(Charset.forName("UTF-8")));
		return restTemplate;
	}
}
