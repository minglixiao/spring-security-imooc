package com.imooc.security.app;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;

/**
 * 实现认证服务器。
 * spring security oauth已经帮我们实现了4种授权模式。
 */
@Configuration
@EnableAuthorizationServer
public class ImoocAuthorizationServerConfig {

}
