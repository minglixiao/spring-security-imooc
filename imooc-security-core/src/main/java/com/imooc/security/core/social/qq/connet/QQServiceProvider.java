package com.imooc.security.core.social.qq.connet;

import com.imooc.security.core.social.qq.api.QQ;
import com.imooc.security.core.social.qq.api.QQImpl;
import org.springframework.social.oauth2.AbstractOAuth2ServiceProvider;

/**
 * 服务提供商的抽象。不同服务器提供商需要不同的实现类。
 * 要继承AbstractOAuth2ServiceProvider。泛型是ApiBinding接口的类型。
 */
public class QQServiceProvider extends AbstractOAuth2ServiceProvider<QQ> {

	private String appId;

	private static final String URL_AUTHORIZE = "https://graph.qq.com/oauth2.0/authorize";

	private static final String URL_ACCESS_TOKEN = "https://graph.qq.com/oauth2.0/token";

	/**
	 * ServiceProvider需要的OAuth2Operations实现类
	 */
	public QQServiceProvider(String appId, String appSecret) {
		/* 父类构造方法需要OAuth2Operations接口的实现类。此处我们使默认的实现类OAuth2Template。
		   OAuth2Template构造函数参数详解：
				clientId : 应用的id。这4个参数，都可在QQ互联官网查看。
				clientSecret: 应用的密码
				authorizeUrl: “spring social逻辑流程图” 第1步，将用户导向认证服务器 的URL
				accessTokenUrl: “spring social逻辑流程图” 第4步，根据授权码申请令牌 的URL
		 */
		//super(new OAuth2Template(appId, appSecret, URL_AUTHORIZE, URL_ACCESS_TOKEN));
		// 替换为自定义的Auth2Template
		super(new QQOAuth2Template(appId, appSecret, URL_AUTHORIZE, URL_ACCESS_TOKEN));
		this.appId = appId;
	}

	/**
	 * ServiceProvider需要的ApiBinding实现类
	 * @param accessToken : spring social会传给我们
	 */
	@Override
	public QQ getApi(String accessToken) {
		// 此处new QQImpl() 是多例的。注意不要使用单例的。
		return new QQImpl(accessToken, appId);
	}
}
