package com.imooc.security.core.validate.code;

import com.imooc.security.core.properties.SecurityProperties;
import com.imooc.security.core.validate.code.sms.DefaultSmsCodeSender;
import com.imooc.security.core.validate.code.sms.SmsCodeSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ValidateCodeBeanConfig {
	
	@Autowired
	private SecurityProperties securityProperties;

	/**
	 *  3. 	@Bean：将默认的验证码生成逻辑作为一个Bean，注入到spring，Bean的名称就是方法名称。
	 *  	@ConditionalOnMissingBean： 如果项目其他地方没有imageValidateCodeGenerator这个bean，
	 *  		就采用当前这个imageValidateCodeGenerator。
	 */
	@Bean
	@ConditionalOnMissingBean(name = "imageValidateCodeGenerator")
	public ValidateCodeGenerator imageValidateCodeGenerator() {
		ImageCodeGenerator codeGenerator = new ImageCodeGenerator(); 
		codeGenerator.setSecurityProperties(securityProperties);
		return codeGenerator;
	}

	/**
	 * @ConditionalOnMissingBean(SmsCodeSender.class) 可以直接写接口名，如：SmsCodeSender.class。
	 * 含义：如果spring上下文中没有找到SmsCodeSender接口的实现类，就是用这个Bean。
	 */
	@Bean
	@ConditionalOnMissingBean(SmsCodeSender.class)
	public SmsCodeSender smsCodeSender() {
		return new DefaultSmsCodeSender();
	}

}
