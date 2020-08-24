package com.imooc.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import com.imooc.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 不需要加@component，因为spring在识别这个类继承了ConstraintValidator会自动加载
 * ConstraintValidator<MyConstraint, Object> 泛型1是自定义校验的注解，泛型2是注解可以判断的数据类型
 */
public class MyConstraintValidator implements ConstraintValidator<MyConstraint, Object> {

    // 随便引用的一个类
    @Autowired
    private HelloService helloService;

    /** 在这个类初始化时执行 */
    @Override
    public void initialize(MyConstraint constraintAnnotation) {
        System.out.println("自定义校验注解的逻辑类，被初始化");
    }

    /** 返回true。代表校验通过。false代表校验不通过。
     * value：校验的对象
     * ConstraintValidatorContext：校验的上下文
     * */
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        helloService.greeting(value.toString());
        return true;
    }
}