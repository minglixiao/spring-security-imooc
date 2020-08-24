package com.imooc.web.config;

import com.imooc.web.interceptor.TimeInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 *  配置Interceptor拦截器。
 *  */
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

    // 将我们写的拦截器注入进来
    @Autowired
    private TimeInterceptor timeInterceptorMy;

    /**
     * 异步处理配置
     */
    /*@Override
    public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
        *//* 一般的拦截器是无法拦截Callable和DeferredResult的接口，需要在这里单独设置他们的拦截器。
           这里的参数和之前写的Interceptor类似，但不是同一个类。
         *//*
        configurer.registerCallableInterceptors(null);
        configurer.registerDeferredResultInterceptors(null);
        // 设置 如果异步响应超过n毫秒还不返回 视为失败
        configurer.setDefaultTimeout(5000);
        *//* 注意Callable，每次访问都是另一起一个线程，之后再关闭。
        我们可以在这里传入一个线程池，让Callable能够重复利用线程池里的线程。
         *//*
        configurer.setTaskExecutor(null);
    }*/

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 将我们的拦截器添加到InterceptorRegistry
        // registry.addInterceptor(timeInterceptorMy);
    }
}