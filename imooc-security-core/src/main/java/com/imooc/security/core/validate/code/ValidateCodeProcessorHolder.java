package com.imooc.security.core.validate.code;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Map;


/**
 *  系统中的校验码处理器。
 */
@Component
public class ValidateCodeProcessorHolder {

	/**
	 * @Autowired 且类型是Map，此处的作用：在项目启动时，收集项目中（spring上下文）所有的
	 * ValidateCodeProcessor接口实现类Bean，放到map中，map的key是 实现类Bean的名称，map的value是实现类Bean。
	 * 当前项目map中有两个元素，分别是smsValidateCodeProcessor和imageValidateCodeProcessor
	 */
	@Autowired
	private Map<String, ValidateCodeProcessor> validateCodeProcessors;

	/**
	 * 根据验证码类型 获取 相应的验证码校验逻辑类（也就是相应的ValidateCodeProcessor接口实现类）
	 */
	public ValidateCodeProcessor findValidateCodeProcessor(ValidateCodeType type) {
		return findValidateCodeProcessor(type.toString().toLowerCase());
	}

	public ValidateCodeProcessor findValidateCodeProcessor(String type) {
		String name = type.toLowerCase() + ValidateCodeProcessor.class.getSimpleName();
		ValidateCodeProcessor processor = validateCodeProcessors.get(name);
		if (processor == null) {
			throw new ValidateCodeException("验证码处理器" + name + "不存在");
		}
		return processor;
	}
}
