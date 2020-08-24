/*
// 这个类和ImoocResourcesServerConfig功能相同，后续就用ImoocResourcesServerConfig
package com.imooc.security.app;

import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class ImoocWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

    //
    // 通过HttpSecurity实现Security的自定义过滤配置。
    //
   @Override
    public void configure(HttpSecurity http) throws Exception {
        // 关闭csrf
        http.csrf().disable();
        // 放行
        http.requestMatchers().antMatchers("/oauth/**","/login/**","/logout/**")
            .and()
            .authorizeRequests()
            .antMatchers("/oauth/**").authenticated()
            .and()
            .formLogin().permitAll(); //新增login form支持用户登录及授权
    }
}
*/