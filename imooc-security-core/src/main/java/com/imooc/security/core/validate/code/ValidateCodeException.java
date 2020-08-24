package com.imooc.security.core.validate.code;

import org.springframework.security.core.AuthenticationException;

/**
 * AuthenticationException是spring security认证过程抛出所有异常的基类。
 * 此处我们自定义一个图形验证码校验异常的类
 */
public class ValidateCodeException extends AuthenticationException {

	private static final long serialVersionUID = -7285211528095468156L;

	public ValidateCodeException(String msg) {
		super(msg);
	}
}
