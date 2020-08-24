package com.imooc.security.core.social.qq.config;

import com.imooc.security.core.properties.QQProperties;
import com.imooc.security.core.properties.SecurityProperties;
import com.imooc.security.core.social.qq.connet.QQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.social.SocialAutoConfigurerAdapter;
import org.springframework.context.annotation.Configuration;
import org.springframework.social.connect.ConnectionFactory;

/**
 * 要继承SocialAutoConfigurerAdapter。
 * @ConditionalOnProperty的作用：当项目的配置文件配置了imooc.security.social.qq.app-id，这个配置类才会生效。
 */
@Configuration
@ConditionalOnProperty(prefix = "imooc.security.social.qq", name = "app-id")
public class QQAutoConfig extends SocialAutoConfigurerAdapter {

	@Autowired
	private SecurityProperties securityProperties;

	/**
	 * 读取配置文件，创建ConnectionFactory。
	 */
	@Override
	protected ConnectionFactory<?> createConnectionFactory() {
		QQProperties qqConfig = securityProperties.getSocial().getQq();
		return new QQConnectionFactory(qqConfig.getProviderId(), qqConfig.getAppId(), qqConfig.getAppSecret());
	}
}
