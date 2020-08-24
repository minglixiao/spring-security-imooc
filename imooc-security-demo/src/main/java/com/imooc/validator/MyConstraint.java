package com.imooc.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * 自定义一个用来校验的注解
 */
// 指定这个注解可以使用在方法上和属性上。
@Target({ElementType.METHOD, ElementType.FIELD})
// 加载运行环境
@Retention(RetentionPolicy.RUNTIME)
// @Constraint指定这个注解的具体校验逻辑在哪个类
@Constraint(validatedBy = MyConstraintValidator.class)
public @interface MyConstraint {

    // 下面3个属性，必写
    String message();

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}