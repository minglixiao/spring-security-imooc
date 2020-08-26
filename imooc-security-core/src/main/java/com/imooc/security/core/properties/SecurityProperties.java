package com.imooc.security.core.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

// 读取配置文件中所有以imooc.security开头的配置项
@ConfigurationProperties(prefix = "imooc.security")
public class SecurityProperties {

	// 读取配置文件中所有以imooc.security.browser开头的配置项
	private BrowserProperties browser = new BrowserProperties();

	private OAuth2Properties oauth2 = new OAuth2Properties();

	/** 这里有个奇怪的问题。spring可以读取配置文件内容到成员变量browser，却读不进code。我弄了一下午也没解决。
	 *  第二天早上，这个问题就不存在了。我也不清楚为什么。
	 */
	// 验证码配置
	private ValidateCodeProperties code = new ValidateCodeProperties();

	private SocialProperties social = new SocialProperties();

	public BrowserProperties getBrowser() {
		return browser;
	}

	public void setBrowser(BrowserProperties browser) {
		this.browser = browser;
	}

	public ValidateCodeProperties getCode() {
		return code;
	}

	public void setCode(ValidateCodeProperties code) {
		this.code = code;
	}

	public SocialProperties getSocial() {
		return social;
	}

	public void setSocial(SocialProperties social) {
		this.social = social;
	}

	public OAuth2Properties getOauth2() {
		return oauth2;
	}

	public void setOauth2(OAuth2Properties oauth2) {
		this.oauth2 = oauth2;
	}
}

