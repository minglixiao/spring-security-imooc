package com.imooc.security.app;

import com.imooc.security.core.properties.OAuth2ClientProperties;
import com.imooc.security.core.properties.SecurityProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.builders.InMemoryClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import java.util.Arrays;

/**
 * 实现认证服务器。
 * spring security oauth已经帮我们实现了4种授权模式。
 * 继承AuthorizationServerConfigurerAdapter，就可以自定义配置
 */
@Configuration
@EnableAuthorizationServer
public class ImoocAuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService  userDetailsService;

    @Autowired
    private SecurityProperties securityProperties;

    @Qualifier("redisTokenStore")
    @Autowired
    public TokenStore tokenStore;

    /**
     * 端点安全性的配置
     * 就是在配置TokenEndpoint，TokenEndpoint在"6.3 Spring Security OAuth核心源码解析"已介绍
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        // 当继承AuthorizationServerConfigurerAdapter，我们需要指定这两个参数。如果不继承，会自动帮我们指定。
        endpoints.authenticationManager(authenticationManager).userDetailsService(userDetailsService);
        // 服务重启，令牌就失效了。如果解决：使用redis、mysql...存取令牌
        endpoints.tokenStore(tokenStore);
    }

    /**
     * 第三方应用客户端的配置。
     * 配置：有哪些应用会访问的我们的系统，认证服务器会给哪些应用发送令牌。
     * 一旦我们重写了这个方法，配置文件中security.oauth2.client.clientId/clientSecret就失效了
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        /* 1. 不通过配置文件去配置
        // 第三方应用客户端信息是存在内存还是数据库。inMemory：内存。jdbc:数据库。
        // 我们项目第三方应用已确定就用inMemory，像QQ，微信的认证服务器就需要选择jdbc
        clients.inMemory()
                // 客户端的clientId和clientSecret
                .withClient("imooc").secret("imoocsecret")
                // 令牌有效时间。单位：秒。0是不过期的
                .accessTokenValiditySeconds(2*60*60)
                // 这个客户端支持哪些授权模式
                .authorizedGrantTypes("refresh_token","password")
                // 能发出去的权限有哪些，可以理解OAuth的权限。客户端发送请求中参数scope要属于该集合中的一员
                .scopes("all","read")
                // 支持多个应用客户端，and()后面可以继续配置其他的应用客户端
                .and()
                .withClient("第二个clientId").secret("第二个clientSecret");*/

        // 2. 重构，通过配置文件去配置
        InMemoryClientDetailsServiceBuilder inMemory = clients.inMemory();
        OAuth2ClientProperties[] clientsInCustom = securityProperties.getOauth2().getClients();
        for (OAuth2ClientProperties p : clientsInCustom) {
            inMemory.withClient(p.getClientId())
                    .secret(p.getClientSecret())
                    //.redirectUris(p.getRedirectUris())
                    //.authorizedGrantTypes(p.getAuthorizedGrantTypes())
                    .accessTokenValiditySeconds(p.getAccessTokenValiditySeconds())
                    //.scopes(p.getScopes())
                    ;
        }
        logger.info(Arrays.toString(clientsInCustom));
    }
}
