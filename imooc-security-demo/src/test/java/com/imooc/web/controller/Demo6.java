package com.imooc.web.controller;

public class Demo6 extends Thread {

    public volatile boolean flag = true;

    public Demo6(String name){
        super(name);
    }

    public synchronized void run() {
        int i = 0 ;
        while(flag){
            try {
                Thread.sleep(900);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(flag){
                // 被强制结束
            }else{
                // 到了5分钟，自动结束
            }

            System.out.println("狗娃线程累计:"+i);
            i++;
        }
    }


    public static void main(String[] args) {
        Demo6 d = new Demo6("狗娃");
        d.start();

        for(int i = 0 ; i<100 ; i++){
            System.out.println("主线程累计："+i);
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // 主线程1=50时，结束狗娃线程
            if(i==50){
                System.out.println("狗娃线程已结束...........");
                d.flag = false;
            }
        }
    }
}