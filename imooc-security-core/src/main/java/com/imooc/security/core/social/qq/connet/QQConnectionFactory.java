package com.imooc.security.core.social.qq.connet;

import com.imooc.security.core.social.qq.api.QQ;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;

/**
 * 创建Connection的工厂。
 * 泛型：自定义ApiBinding的数据类型。
 */
public class QQConnectionFactory extends OAuth2ConnectionFactory<QQ> {

	public QQConnectionFactory(String providerId, String appId, String appSecret) {
		// 创建ConnectionFactory需要这些参数providerId、ServiceProvider、Adapter
		super(providerId, new QQServiceProvider(appId, appSecret), new QQAdapter());
	}
}
