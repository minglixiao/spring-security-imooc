package com.imooc.security;

import com.imooc.security.core.authorize.AuthorizeConfigProvider;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.stereotype.Component;

/**
 */
@Component
@Order(Integer.MAX_VALUE)
public class DemoAuthorizeConfigProvider implements AuthorizeConfigProvider {

    @Override
    public void config(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry config) {
        config.antMatchers(
                "/user/regist", // 注册请求
                // org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController
                // BasicErrorController 类提供的默认错误信息处理服务
                "/error",
                "/connect/*",
                "/auth/*",
                "/signin",
                "/social/signUp",  // app注册跳转服务
                "/swagger-ui.html",
                "/swagger-ui.html/**",
                "/webjars/**",
                "/swagger-resources/**",
                "/v2/**"
        ).permitAll()
        .antMatchers("/user").hasRole("test28");
        config.anyRequest().access("@rbacService.hasPermission(request,authentication)");
    }
}
