package com.imooc.security.app;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

/**
 * 令牌的存取
 */
@Configuration
public class TokenStoreConfig {

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Bean
    public RedisTokenStore redisTokenStore() {
        return  new RedisTokenStore(redisConnectionFactory);
    }
}
