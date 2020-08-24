package com.imooc.web.interceptor;

import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * Interceptor拦截器开发步骤：
 *  1. 必须实现HandlerInterceptor接口。
 *  2. 加注解@Component
 *  3. 必须要配置
 * 留意下，这个接口的方法和方法的参数。拦截器只能获取controller类名和其方法名。但是无法获取方法的参数。
 *
 */
@Component
public class TimeInterceptor implements HandlerInterceptor {

    /**
     * “controller方法” 执行前，调用这个方法。
     * Object handler: 这个对象记录了本次请求是由哪个controller类和controller类中的方法处理的。
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        System.out.println("我的TimeInterceptor---preHandle");
        // 获取处理请求的controller类名
        System.out.println("我的TimeInterceptor---preHandle---controller类："+((HandlerMethod)handler).getBean().getClass().getName());
        // 获取处理请求的controller方法
        System.out.println("我的TimeInterceptor---preHandle---controller类的方法："+((HandlerMethod)handler).getMethod().getName());
        // 由于拦截器分为：preHandle、postHandle、afterCompletion，我们需要把数据放到request里面，才能在后面方法中获取到。
        request.setAttribute("startTime", new Date().getTime());
        // 返回true，代表放行。返回false，不放行。
        return true;
    }

    /**
     * “controller方法” 正常执行完，调用这个方法。如果controller方法报错了，则不调用这个方法。
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,ModelAndView modelAndView) throws Exception {
        System.out.println("我的TimeInterceptor---postHandle");
        Long start = (Long) request.getAttribute("startTime");
        System.out.println("我的TimeInterceptor---postHandle---耗时:"+ (new Date().getTime() - start));
    }

    /**
     * 无论“controller方法” 正常执行还是报错，都会调用这个方法。
     * 参数Exception：controller方法报错后，这个参数才有值。
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        System.out.println("我的TimeInterceptor---afterCompletion");
        Long start = (Long) request.getAttribute("startTime");
        System.out.println("我的TimeInterceptor---afterCompletion---耗时:"+ (new Date().getTime() - start));
        System.out.println("我的TimeInterceptor---afterCompletion---异常是： "+ex);
    }
}
