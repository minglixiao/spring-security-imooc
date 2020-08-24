package com.imooc.security.core.validate.code.sms;

import com.imooc.security.core.properties.SecurityProperties;
import com.imooc.security.core.validate.code.ValidateCode;
import com.imooc.security.core.validate.code.ValidateCodeGenerator;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;


@Component("smsValidateCodeGenerator")
public class SmsCodeGenerator implements ValidateCodeGenerator {

	@Autowired
	private SecurityProperties securityProperties;

	@Override
	public ValidateCode generate(ServletWebRequest request) {
		String code = RandomStringUtils.randomNumeric(securityProperties.getCode().getSms().getLength());
		System.out.println("生成的手机短信验证码："+ code +
				"POST /authentication/mobile HTTP/1.1 \n" +
				"Host: 127.0.0.1:8060\n" +
				"Content-Type: application/x-www-form-urlencoded\n" +
				"deviceId: 007\n" +
				"Authorization: Basic aW1vb2M6aW1vb2NzZWNyZXQ=\n" +
				"Cache-Control: no-cache\n" +
				"Postman-Token: cf894c8c-5870-4742-ac52-d8f84d36cf1f\n" +
				"\n" +
				"mobile=130123456789&smsCode=838611");
		return new ValidateCode(code, securityProperties.getCode().getSms().getExpireIn());
	}

	public SecurityProperties getSecurityProperties() {
		return securityProperties;
	}

	public void setSecurityProperties(SecurityProperties securityProperties) {
		this.securityProperties = securityProperties;
	}
	
	

}
