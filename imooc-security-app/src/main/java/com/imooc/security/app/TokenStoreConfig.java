package com.imooc.security.app;

import com.imooc.security.app.jwt.ImoocJwtTokenEnhancer;
import com.imooc.security.core.properties.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

/**
 * 令牌的存取
 */
@Configuration
public class TokenStoreConfig {

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Bean
    /*
     * 最终效果：imooc.security.oauth2.storeType等于redis的时候，下面的配置会生效
     */
    @ConditionalOnProperty(prefix = "imooc.security.oauth2", name = "storeType", havingValue = "redis")
    public TokenStore redisTokenStore() {
        return  new RedisTokenStore(redisConnectionFactory);
    }


    /**
     *  prefix: 配置文件配置项的前缀，最后一个点前面的部分。name: 最后一个点后面的部分。此处他两组合是imooc.security.oauth2.storeType
     *  havingValue: 当配置项的值是jwt时，这个类里面的配置都会生效。
     *  matchIfMissing : 当没有相应的配置项（此处就是imooc.security.oauth2.storeType）时，这个类里面的配置是否会生效。true:会。
     *  此处的效果：imooc.security.oauth2.storeType等于jwt的时候 或者 没有配置的时候，下面的配置会生效
     */
    @Configuration
    @ConditionalOnProperty(prefix = "imooc.security.oauth2", name = "storeType", havingValue = "jwt", matchIfMissing = true)
    public static class JwtTokenConfig {

        @Autowired
        private SecurityProperties securityProperties;

        /**
         * TokenStore：存取token
         */
        @Bean
        public TokenStore jwtTokenStore() {
            return new JwtTokenStore(jwtAccessTokenConverter());
        }

        /**
         * JwtAccessTokenConverter : token生成中的处理
         */
        @Bean
        public JwtAccessTokenConverter jwtAccessTokenConverter() {
            JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
            /* 设置密钥。发出去的令牌用密钥进行签名，验证令牌用密钥进行验签。
               唯一的安全性，如果密钥泄露，别人用我们的密钥签发，可随意进入我们的系统。
             */
            converter.setSigningKey(securityProperties.getOauth2().getJwtSigningKey());
            return converter;
        }

        /**
         * TokenEnhancer: 令牌增强（扩展）
         *
         */
        @Bean
        /**
         * 不能使用@ConditionalOnMissingBean(TokenEnhancer.class)，因为JwtAccessTokenConverter也是一个TokenEnhancer
         而@ConditionalOnBean(TokenEnhancer.class)是必须存在一个TokenEnhancer的时候，才被创建
         先不纠结这个问题了。就这样吧。也就是封装程度的问题
         */
        @ConditionalOnBean(TokenEnhancer.class)
        public TokenEnhancer jwtTokenEnhancer() {
            return new ImoocJwtTokenEnhancer();
        }
    }
}
