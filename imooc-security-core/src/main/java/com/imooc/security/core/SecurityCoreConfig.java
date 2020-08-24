package com.imooc.security.core;

import com.imooc.security.core.properties.SecurityProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;

/**  让注解@ConfigurationProperties生效  */
@Configuration
@EnableConfigurationProperties(SecurityProperties.class)
public class SecurityCoreConfig {

    @Bean
    public PasswordEncoder passwordEncoder(){
        /* BCryptPasswordEncoder是一个实现PasswordEncoder接口的实现类，采用BCrypt加密算法。
           如果你想采用md5加密方法，只需要实现PasswordEncoder接口，自己重写他的两个方法即可。
         */


        new DefaultTokenServices();
        return new BCryptPasswordEncoder();
    }

}
