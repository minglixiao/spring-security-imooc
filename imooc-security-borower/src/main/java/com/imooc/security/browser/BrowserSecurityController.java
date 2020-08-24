package com.imooc.security.browser;

import com.imooc.security.browser.support.SocialUserInfo;
import com.imooc.security.core.properties.SecurityProperties;
import com.imooc.security.core.support.SimpleResponse;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class BrowserSecurityController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SecurityProperties securityProperties;

    private RequestCache requestCache = new HttpSessionRequestCache();

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Autowired
    private ProviderSignInUtils providerSignInUtils;

    /**
     * 跳回到之前请求上。同步的，可能不是异步。json格式的用户信息。给他跳转是不合适的。
     * 登录成功后会被调用，封装我们的认证信息：ip  session  userdetails
     * json返回回去。ObjectMapper，将对象转化为JSON。
     * 配置：
     */

    /**
     * 当需要身份认证时，跳转到这里
     */
    @RequestMapping("/authentication/require")
    // 设置本RESTful服务的响应状态码是401
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    public SimpleResponse requireAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
       /* 用户访问接口（如：127.0.0.1:8060/user），如果没有经过登录认证，security会跳转到本接口
    （如：127.0.0.1:8060/authentication/require）。HttpSessionRequestCache对象的作用就是缓存
     引发跳转的请求（也就是127.0.0.1:8060/user）。
     */
        SavedRequest savedRequest = requestCache.getRequest(request, response);
        if (savedRequest != null) {
            // 获取 引发跳转请求的URL。如：127.0.0.1:8060/user、127.0.0.1:8060/index.html
            String targetUrl = savedRequest.getRedirectUrl();
            logger.info("引发跳转的请求是:" + targetUrl);
            // 引发跳转的请求 以.html结尾（如用户访问：127.0.0.1:8060/index.html），则跳转到自定义的登陆页面。
            if (StringUtils.endsWithIgnoreCase(targetUrl, ".html")) {
                // RedirectStrategy是一个spring security 重定向（跳转）的工具类。第三个参数在下一个章节详解
                redirectStrategy.sendRedirect(request, response,  securityProperties.getBrowser().getLoginPage());
                // 为什么我这边找不到这个URL呢？
                logger.info("系统配置封装：" + securityProperties.getBrowser().getLoginPage());
            }
        }
        //  引发跳转的请求 不是以.html结尾（如用户访问：127.0.0.1:8060/user），返回一个JSON数据。
        return new SimpleResponse("访问的服务需要身份认证，请引导用户到登录页");
    }


    /**
     * 拿到spring social中服务提供商提供的用户信息
     */
    @GetMapping("/social/user")
    public SocialUserInfo getSocialUserInfo(HttpServletRequest request) {
        SocialUserInfo userInfo = new SocialUserInfo();
        //  spring  social 将 服务提供商提供的用户信息  放到session中， 根据request能够获取到
        Connection<?> connection = providerSignInUtils.getConnectionFromSession(new ServletWebRequest(request));
        userInfo.setProviderId(connection.getKey().getProviderId());
        userInfo.setProviderUserId(connection.getKey().getProviderUserId());
        userInfo.setNickname(connection.getDisplayName());
        userInfo.setHeadimg(connection.getImageUrl());
        return userInfo;
    }

    @GetMapping("/session/invalid")
    // 设置本RESTful服务的响应状态码是401
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    public SimpleResponse sessionInvalid(){
        return new SimpleResponse("session失效");
    }


}

