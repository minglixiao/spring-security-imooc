package com.imooc.security.core.validate.code.sms;

/**
 * 发送手机验证码的默认实现类
 */
public class DefaultSmsCodeSender implements SmsCodeSender {

	@Override
	public void send(String mobile, String code) {
		System.out.println("向手机"+mobile+"发送短信验证码"+code);
	}
}
