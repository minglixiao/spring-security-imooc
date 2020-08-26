package com.imooc.security.app;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.social.security.SpringSocialConfigurer;
import org.springframework.stereotype.Component;

/**
 * 实现BeanPostProcessor
 */
@Component
public class SpringSocialConfigurerPostProcessor implements BeanPostProcessor {

    // 任何bean初始化之前调用该方法
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    /**
     * 任何bean初始化之后调用该方法
     * 目的：浏览器环境下不要去改变返回值。APP环境下修改返回值SpringSocialConfigurer的属性。
     * 目的：当SpringSocialConfigurer初始化之后修改其属性signupUrl
     * @param bean  : 注入到spring的bean
     * @param beanName ：bean的名称
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (beanName.equals("imoocSocialSecurityConfig")) {
            SpringSocialConfigurer config = (SpringSocialConfigurer) bean;
            config.signupUrl("/social/signUp");
            return bean;
        }
        return bean;
    }
}
