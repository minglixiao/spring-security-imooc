package com.imooc.security.core.properties;

import org.springframework.boot.autoconfigure.social.SocialProperties;

/**
 * 继承SocialProperties，父类有两个成员变量: appId、appSecret。
 */
public class QQProperties extends SocialProperties {

	// 服务提供商的唯一标识。默认就是qq
	private String providerId = "qq";

	public String getProviderId() {
		return providerId;
	}

	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}
	
}
