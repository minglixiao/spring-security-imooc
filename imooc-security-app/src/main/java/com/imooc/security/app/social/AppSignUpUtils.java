package com.imooc.security.app.social;

import com.imooc.security.app.AppConstants;
import com.imooc.security.app.AppSecretException;
import com.imooc.security.core.social.SignUpUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;

import java.util.concurrent.TimeUnit;

/**
 * 基于redis，存取deviceId和第三方用户信息
 */
@Component
public class AppSignUpUtils implements SignUpUtils {

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Autowired
    private UsersConnectionRepository usersConnectionRepository;

    @Autowired
    private ConnectionFactoryLocator connectionFactoryLocator;

    /**
     * 存deviceId和第三方用户信息
     */
    @Override
    public void saveConnection(ServletWebRequest request, ConnectionData connectionData) {
        redisTemplate.opsForValue().set(buildKey(request), connectionData,10, TimeUnit.MINUTES);
    }

    /**
     * 用户注册完了之后，绑定userId和第三方用户信息
     */
    @Override
    public void doPostSignUp(String userId, ServletWebRequest request) {
        String key = buildKey(request);
        if(!redisTemplate.hasKey(key)){
            throw new AppSecretException("无法找到缓存的第三方用户社交账号信息");
        }
        ConnectionData connectionData = (ConnectionData) redisTemplate.opsForValue().get(key);
        // 将userId和第三方用户信息插入到数据表userconnection
        usersConnectionRepository.createConnectionRepository(userId).addConnection(getConnection(connectionFactoryLocator, connectionData));
        redisTemplate.delete(key);
    }

    /**
     * 根据ConnectionData创建Connection（就是userconnection数据表一条记录）
     */
    public Connection<?> getConnection(ConnectionFactoryLocator connectionFactoryLocator, ConnectionData connectionData) {
        return connectionFactoryLocator.getConnectionFactory(connectionData.getProviderId()).createConnection(connectionData);
    }

    private String buildKey(ServletWebRequest request) {
        String deviceId = request.getHeader(AppConstants.DEFAULT_HEADER_DEVICE_ID);
        if (StringUtils.isBlank(deviceId)) {
            throw new AppSecretException("设备id参数不能为空");
        }
        return "imooc:security:social.connect." + deviceId;
    }
}
