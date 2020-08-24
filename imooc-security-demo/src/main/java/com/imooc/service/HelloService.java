package com.imooc.service;

import org.springframework.stereotype.Service;

@Service
public class HelloService {

    public  String  greeting(String str){
        System.out.println("收到参数："+str);
        return str;
    }
}
