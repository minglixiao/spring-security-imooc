package com.imooc.web.async;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * 实现ApplicationListener，spring  boot容器初始化完成后，调用该类。
 */
@Component
public class QueueListener implements ApplicationListener<ContextRefreshedEvent> {

    private Logger logger =  LoggerFactory.getLogger(QueueListener.class);

    @Autowired
    private  MockQueue mockQueue;

    @Autowired
    private  DeferredResultHolder deferredResultHolder;

    /**
     * 3. 线程2，监听处理结果
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        new Thread(()->{
            while (true){
                if(StringUtils.isNoneBlank(mockQueue.getCompleteOrder())){
                    String  orderNumber = mockQueue.getCompleteOrder();
                    logger.info("返回订单处理结果："+orderNumber);
                    DeferredResult<String> deferredResult = deferredResultHolder.getMap().get(orderNumber);
                    /* 一旦调用DeferredResult对象的setResult方法。spring根据对象的hash值，
                    找到当初那个请求（request，response），把那个请求响应给前端，并设置返回值。
                     */
                    deferredResult.setResult("当我调用该方法，就代表spring可以将当初那个请求响应回去了。");
                    mockQueue.setCompleteOrder(null);
                }else{
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
