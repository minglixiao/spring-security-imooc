package com.imooc.security.core.social;

import org.springframework.social.security.SocialAuthenticationFilter;
import org.springframework.social.security.SpringSocialConfigurer;

/**
 * 自定义SpringSocialConfigurer的实现类
 */
public class ImoocSpringSocialConfigurer extends SpringSocialConfigurer {
	
	private String filterProcessesUrl;
	
	public ImoocSpringSocialConfigurer(String filterProcessesUrl) {
		this.filterProcessesUrl = filterProcessesUrl;
	}

    /**
     * 重写父类的postProcess方法
     */
	@Override
	protected <T> T postProcess(T object) {
		// 获取SocialAuthenticationFilter
		SocialAuthenticationFilter filter = (SocialAuthenticationFilter) super.postProcess(object);
		// 指定拦截URL以 某某 开头的请求
		filter.setFilterProcessesUrl(filterProcessesUrl);
		return (T) filter;
	}
}
