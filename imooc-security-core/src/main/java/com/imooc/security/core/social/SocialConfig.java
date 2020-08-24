package com.imooc.security.core.social;

import com.imooc.security.core.properties.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurerAdapter;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.social.security.SpringSocialConfigurer;
import javax.sql.DataSource;

/**
 * 配置spring social
 */
@Configuration
@EnableSocial
public class SocialConfig extends SocialConfigurerAdapter {

	@Autowired
	private DataSource dataSource;

	@Autowired
	private SecurityProperties securityProperties;

	// 如果项目不存在ConnectionSignUp实现类，就不用注入了。
	@Autowired(required = false)
	private ConnectionSignUp connectionSignUp;

	/**
	 * 配置UsersConnectionRepository
	 */
	@Override
	@Primary
	@Bean
	public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
		/*	DataSource : 数据源
			ConnectionFactoryLocator : 这个类的作用是查找项目中的ConnectionFactory
			TextEncryptor : 对插入到数据库的数据 进行加解密。Encryptors.noOpText(): 不做加解密。
		 */
		JdbcUsersConnectionRepository repository = new JdbcUsersConnectionRepository(dataSource,
				connectionFactoryLocator, Encryptors.noOpText());
		// 默认操作数据表UserConnection，表名不想叫这个，可以给表名加个前缀，在此处设置加的前缀。
		repository.setTablePrefix("imooc_");
		// 如果项目中实现了connectionSignUp（自动注册），就配置进去。
		if(connectionSignUp != null) {
			repository.setConnectionSignUp(connectionSignUp);
		}
		return repository;
	}

	/**
	 * 创建SpringSocialConfigurer的Bean
	 */
	@Bean
	public SpringSocialConfigurer imoocSocialSecurityConfig() {
		String filterProcessesUrl = securityProperties.getSocial().getFilterProcessesUrl();
		ImoocSpringSocialConfigurer configurer = new ImoocSpringSocialConfigurer(filterProcessesUrl);
		// 自定义注册页面的URL
		configurer.signupUrl(securityProperties.getBrowser().getSignUpUrl());
		return configurer;
	}

	/**
	 * 我们需要ProviderSignInUtils这个工具类，注入到我们项目中。
	 * 我们把ConnectionFactoryLocator写在请求参数里，spring boot会直接给我们注入进来。
	 */
	@Bean
	public ProviderSignInUtils providerSignInUtils(ConnectionFactoryLocator connectionFactoryLocator) {
		return new ProviderSignInUtils(connectionFactoryLocator,
				getUsersConnectionRepository(connectionFactoryLocator)) {
		};
	}
}
