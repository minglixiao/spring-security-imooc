package com.imooc.security.browser;

import com.imooc.security.core.authentication.AbstractChannelSecurityConfig;
import com.imooc.security.core.authentication.mobile.SmsCodeAuthenticationSecurityConfig;
import com.imooc.security.core.properties.SecurityConstants;
import com.imooc.security.core.properties.SecurityProperties;
import com.imooc.security.core.validate.code.ValidateCodeSecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.session.InvalidSessionStrategy;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import org.springframework.social.security.SpringSocialConfigurer;

import javax.sql.DataSource;

// 1. 要继承spring security提供的适配器类
@Configuration
public class BrowserSecurityConfig extends AbstractChannelSecurityConfig {

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;

    @Autowired
    private ValidateCodeSecurityConfig validateCodeSecurityConfig;

    @Autowired
    private SpringSocialConfigurer springSocialConfigurer;

    @Autowired
    private InvalidSessionStrategy invalidSessionStrategy;

    @Autowired
    private SessionInformationExpiredStrategy sessionInformationExpiredStrategy;

    @Autowired
    private LogoutSuccessHandler logoutSuccessHandler;

    // 2. 重写configure(HttpSecurity httpSecurity)方法
    @Override
    protected  void configure(HttpSecurity http) throws Exception{
        applyPasswordAuthenticationConfig(http);
        http.apply(validateCodeSecurityConfig)
                .and()
            .apply(smsCodeAuthenticationSecurityConfig)
                .and()
            .apply(springSocialConfigurer)
                .and()
            .rememberMe()
                // 设置TokenRepository
                .tokenRepository(persistentTokenRepository())
                // 设置记住我的时间。单位：秒
                .tokenValiditySeconds(securityProperties.getBrowser().getRememberMeSeconds())
                // 设置我们自定义的UserDetailsService。
                .userDetailsService(userDetailsService)
                .and()
            .sessionManagement()
                //
                .invalidSessionStrategy(invalidSessionStrategy)
                .maximumSessions(securityProperties.getBrowser().getSession().getMaximumSessions())
                .maxSessionsPreventsLogin(securityProperties.getBrowser().getSession().isMaxSessionsPreventsLogin())
                .expiredSessionStrategy(sessionInformationExpiredStrategy)
                .and()
                .and()
            .logout()
                // 重命名退出请求的URL，只是重命名
                .logoutUrl("/singout")
                // 自定义退出成功后跳转的URL
                //.logoutSuccessUrl("")
                // 自定义退出登录后 处理逻辑。与logoutSuccessUrl互斥，有handler则logoutSuccessUrl失效
                .logoutSuccessHandler(logoutSuccessHandler)
                // 删除浏览器的cookie
                .deleteCookies("JSESSIONID")
                .and()
            // 指定授权配置，此处意思：任何请求都需要身份认证
            .authorizeRequests()
                // 自定义登录页面  步骤2： 匹配一个正则表达式，匹配成功的URL不需要身份认证
                // 处理不同类型的请求  步骤2：设置自定义的controller方法，不需要身份认证
                .antMatchers(
                        SecurityConstants.DEFAULT_UNAUTHENTICATION_URL,
                        SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_MOBILE,
                        securityProperties.getBrowser().getLoginPage(),
                        SecurityConstants.DEFAULT_VALIDATE_CODE_URL_PREFIX+"/*",
                        securityProperties.getBrowser().getSignUpUrl(),
                        // securityProperties.getBrowser().getSignOutUrl(),
                        "/user/regist", "/session/invalid")
                        .permitAll()
                // 任何请求
                .anyRequest()
                // 都需要身份认证
                .authenticated()
                .and()
            .csrf().disable(); //关闭CSRF
    }

    /**
     * 配置TokenRepository
     */
    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        // 注入数据源
        tokenRepository.setDataSource(dataSource);
        /* 设置为true，会创建一张数据表，如果数据库已有该张表，程序会报错。建好表之后，这句代码就是注释掉。
           类：JdbcTokenRepositoryImpl 成员变量：CREATE_TABLE_SQL 就是具体SQL。
         */
		// tokenRepository.setCreateTableOnStartup(true);
        return tokenRepository;
    }
}
