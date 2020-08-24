package com.imooc.web.config;

import java.util.ArrayList;
import java.util.List;
import com.imooc.web.filter.TimeFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *  配置Filter
 *  */
//@Configuration
public class WebFilterConfig {

    /**
     * 返回值必须是FilterRegistrationBean。
     */
    //@Bean
    public FilterRegistrationBean timeFilter() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        TimeFilter timeFilter = new TimeFilter();
        registrationBean.setFilter(timeFilter);
        List<String> urls = new ArrayList<>();
        // 这里可以指定拦截那些API
        urls.add("/*");
        registrationBean.setUrlPatterns(urls);
        return registrationBean;
    }
}
