package com.imooc.security.browser.logout;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imooc.security.core.properties.SecurityProperties;
import com.imooc.security.core.support.SimpleResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * 自定义退出成功后的处理
 */
public class MyLogoutSuccessHandler implements LogoutSuccessHandler {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private ObjectMapper objectMapper = new ObjectMapper();

    private SecurityProperties securityProperties;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 当退出成功的时候，如果配置了一个页面，则跳转到页面，没有配置页面则返回一个JSON数据。
        String signOutUrl = securityProperties.getBrowser().getSignOutUrl();
        if (StringUtils.isBlank(signOutUrl)) {
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(objectMapper.writeValueAsString(new SimpleResponse("退出成功")));
        } else {
            response.sendRedirect(signOutUrl);
        }
    }

    public SecurityProperties getSecurityProperties() {
        return securityProperties;
    }

    public void setSecurityProperties(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }
}