package com.imooc.web.async;

import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import java.util.concurrent.Callable;

@RestController
public class AsyncController {

    @Autowired
    private  MockQueue mockQueue;

    @Autowired
    private  DeferredResultHolder deferredResultHolder;

    private Logger logger =  LoggerFactory.getLogger(AsyncController.class);

    /**
     * 演示同步处理Rest
     */
    @RequestMapping("/order")
    public String order () throws  Exception{
        logger.info("主线程开始");
        Thread.sleep(3000);
        logger.info("主线程返回");
        return "success";
    }

    /**
     *  使用Runnable异步处理Rest服务。
     */
    @RequestMapping("/order/1")
    // 1. 要求返回Callable，泛型是接口真实的返回值。
    public Callable<String> order1 () throws  Exception{
        logger.info("主线程开始");
        // 2. Callable：单开一个线程，由spring管理。
        Callable<String> result = new Callable<String>(){
            @Override
            public String call() throws Exception {
                logger.info("副线程开始");
                // 3. 将业务逻辑交由副线程处理
                Thread.sleep(3000);
                logger.info("副线程返回");
                return "success";
            }
        };
        logger.info("主线程返回");
        System.out.println("");
        return result;
    }


    /**
     *  使用DeferredResult异步处理Rest服务。
     */
    @RequestMapping("/order/2")
    // 1.  线程1，接收请求，及时让该线程结束。
    public  DeferredResult<String> order2 () throws  Exception{
        logger.info("主线程开始");
        // 随机生成一个8位的随机数
        String orderNumber = RandomStringUtils.randomNumeric(8);
        // 1.1  将消息 发给 应用2
        mockQueue.setPlaceOrder(orderNumber);
        DeferredResult<String> result = new DeferredResult<>();
        deferredResultHolder.getMap().put(orderNumber,result);
        // 1.2 必须要返回DeferredResult对象。泛型是真正要返回数据的类型
        return result;
    }
}
