package com.imooc.web.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import java.util.Date;

//@Aspect
@Component
public class TimeAspect {

    /*
      @Before() 在 被拦截的方法 前 执行本方法
      @After()  在 被拦截的方法 后 执行本方法
      @AfterThrowing 被拦截的方法 抛出某种异常时，会执行该方法
      @Around  包含上述3种注解的功能，常用。
        execution表达式不懂的话，去百度。如：https://www.jianshu.com/p/d9525c6d4e26
     */
    //@Around("execution(* com.imooc.web.controller.UserController.*(..))")
    /* Object: 可以返回 被拦截方法的返回值。也可以对被拦截方法的返回值做处理后，再返回。
       ProceedingJoinPoint: 被拦截方法的 信息。如：方法的参数。
     */
    public Object handleControllerMethod(ProceedingJoinPoint pjp) throws Throwable {
        System.out.println("我的TimeAspect---handleControllerMethod---开始");
        // 获取到被拦截方法的参数
        Object[]  args  = pjp.getArgs();
        for (Object arg: args) {
            System.out.println("我的TimeAspect---handleControllerMethod---方法参数"+arg);
        }
        Long  startTime = new Date().getTime();
        // 放行 不再拦截
        Object object = pjp.proceed();
        System.out.println("我的TimeAspect---handleControllerMethod---耗时"+ (new Date().getTime()-startTime) );
        System.out.println("我的TimeAspect---handleControllerMethod---结束");
        return object;
    }
}
