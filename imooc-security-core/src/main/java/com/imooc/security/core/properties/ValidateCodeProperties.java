package com.imooc.security.core.properties;

import lombok.Data;

/**
 * 这类里有各种验证码配置。
 */
@Data
public class ValidateCodeProperties {

	// 图形验证码配置
	private ImageCodeProperties image = new ImageCodeProperties();

	private SmsCodeProperties sms = new SmsCodeProperties();

}
