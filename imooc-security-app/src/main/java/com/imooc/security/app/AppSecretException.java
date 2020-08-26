package com.imooc.security.app;

/**
 * 声明一个APP安全异常类
 */
public class AppSecretException extends RuntimeException {

	public AppSecretException(String msg){
		super(msg);
	}
}
