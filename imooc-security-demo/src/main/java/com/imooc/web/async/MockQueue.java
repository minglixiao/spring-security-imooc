package com.imooc.web.async;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 应用2
 */
@Component
public class MockQueue {

    private Logger logger =  LoggerFactory.getLogger(MockQueue.class);

    // 有值代表有待处理的下单
    private String  placeOrder;

    // 有值代表有完成的下单
    private String  completeOrder;

    public String getPlaceOrder() {
        return placeOrder;
    }

    /**
     * 2. 应用2，监听并处理消息。正常工作中，我们会监听消息中间件MQ，收到消息后处理业务逻辑
     * 此处我们简化，收到消息后，启动线程取处理业务逻辑。工作中，我们尽量不这样做。
     */
    public void setPlaceOrder(String placeOrder) {
        new Thread(()->{
            logger.info("接到下单请求："+placeOrder);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.completeOrder = placeOrder;
            logger.info("下单请求处理完毕 ："+placeOrder);
        }).start();
    }

    public String getCompleteOrder() {
        return completeOrder;
    }

    public void setCompleteOrder(String completeOrder) {
        this.completeOrder = completeOrder;
    }
}
