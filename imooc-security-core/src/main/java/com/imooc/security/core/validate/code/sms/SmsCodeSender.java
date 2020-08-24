package com.imooc.security.core.validate.code.sms;

/**
 * 发送手机验证码的接口
 */
public interface SmsCodeSender {
	
	void send(String mobile, String code);

}
