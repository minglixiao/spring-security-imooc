package com.imooc.security.core.social;

import org.springframework.social.connect.ConnectionData;
import org.springframework.web.context.request.ServletWebRequest;

public interface SignUpUtils {

    void saveConnection(ServletWebRequest request, ConnectionData connectionData);

    void doPostSignUp(String userId, ServletWebRequest request);
}
