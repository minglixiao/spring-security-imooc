package com.imooc.web.filter;

import java.io.IOException;
import java.util.Date;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.springframework.stereotype.Component;

/* 方式1： 在类上加@Component就是拦截所有的API，此时我们不需要写配置Filter拦截器类。
 * 方式2： 不在类上加@Component，另写一个配置Filter拦截器类。我们推荐这种。该放手适用于：
 *      1. 我们需要指定拦截的API，不是拦截所有的API
 *      2. Filter是第三方的，我们无法加@Component
 */
//@Component
public class TimeFilter implements Filter {

    // 初始化时调用。仅在项目启动时，调用一次
    @Override
    public void init(FilterConfig arg0) throws ServletException {
        System.out.println("我的TimeFilter---init");
    }

    /**
    留意本方法的参数。 FilterChain chain:过滤器链。一参和二参：说明本方法内可以获取request和response对象。
    本方法没有其他参数了，也注定filter无法获取到我们的controller类和controller类中方法。
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        System.out.println("我的TimeFilter---doFilter---start");
        long start = new Date().getTime();
        // 这个过滤器 放行 对接口的请求
        chain.doFilter(request, response);
        System.out.println("我的TimeFilter---doFilter---接口耗时:"+ (new Date().getTime() - start));
        System.out.println("我的TimeFilter---doFilter---finish");
    }

    //销毁时调用。仅在项目正常结束时，调用一次。
    @Override
    public void destroy() {
        System.out.println("我的TimeFilter---destroy");
    }
}