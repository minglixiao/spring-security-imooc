package com.imooc.security.core.validate.code;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 验证码接口
 */
@RestController
public class ValidateCodeController {

	public static final String SESSION_KEY = "SESSION_KEY_FOR_CODE_";

	/**
	 * 此处我们使用@Autowired，且类型是个Map。他的作用是：在项目启动时，收集系统（spring）
	 * 中所有的ValidateCodeProcessor接口的实现类的Bean，放到map中，map的key是实现类Bean的名称。
	 * map的value就是实现类Bean。我们称之为“依赖查找”。
	 * 当前项目map中有两个元素，分别是smsValidateCodeProcessor和imageValidateCodeProcessor
	 */
	@Autowired
	private Map<String, ValidateCodeProcessor> validateCodeProcessor;

	/**
	 * 所有验证码创建的统一接口，根据验证码类型不同，调用不同的 {@link ValidateCodeProcessor}接口实现
	 */
	@GetMapping("/code/{type}")
	public void createCode(HttpServletRequest request, HttpServletResponse response, @PathVariable String type)
			throws Exception {
		validateCodeProcessor.get(type+"ValidateCodeProcessor").create(new ServletWebRequest(request, response));
	}
	/*  认证之前是token
		认证之后是用户信息
	 */
}
