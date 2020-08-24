package com.imooc.web.controller;

import com.imooc.exception.UserNotExistException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

// 步骤1： @ControllerAdvice : 这个类只处理controller抛出来的异常
@ControllerAdvice
public class ControllerExceptionHandler {

    /**
     * 问：如果有多个异常处理，那谁先谁后呢？
     * 答：异常子类的优先级高，异常父类的优先级第。
     * 异常被此处拦截，spring boot自带的拦截就会失效。异常不被此处拦截，spring boot自带拦截才会生效。
     * 此处的异常拦截，浏览器访问和非浏览器访问，都走这里。接口异常后，都会返回这里的返回值。
     */
    // 步骤2：@ExceptionHandler：当controller抛出指定的异常时，会转到该方法处理。
    @ExceptionHandler(UserNotExistException.class)
    // 将方法返回值转为JSON
    @ResponseBody
    // 步骤3：指定返回的HTTP状态码
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, Object> handleUserNotExistException(UserNotExistException ex) {
        Map<String, Object> result = new HashMap<>();
        result.put("userId", ex.getId());
        result.put("message", ex.getMessage());
        return result;
    }

    /**
     * 如果我们拦截是异常总类Exception，那就是全局异常处理，它能捕获整个项目的异常情况，并返回统一格式的异常结果。
     * 以后项目我改如何实现全局异常处理呢？ 把这个类拷贝过去，返回值根据项目统一标准去更改。
     */
    //@ExceptionHandler(Exception.class)
    //@ResponseBody
    //@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, Object> handleException(Exception ex) {
        // 此处我们会捕获我们所有controller抛出的异常。这里我们需要打印或者日志输出错误信息
        ex.printStackTrace();
        Map<String, Object> result = new HashMap<>();
        result.put("id", 123);
        result.put("message", ex.getMessage());
        return result;
    }
}
