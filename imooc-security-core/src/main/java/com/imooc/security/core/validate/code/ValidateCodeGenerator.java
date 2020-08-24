package com.imooc.security.core.validate.code;

import org.springframework.web.context.request.ServletWebRequest;

/**
 * 1. 将验证码生成逻辑抽象出一个接口。
 */
public interface ValidateCodeGenerator {

	ValidateCode generate(ServletWebRequest request);
}
