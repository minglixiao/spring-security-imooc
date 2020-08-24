package com.imooc.web.async;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.DeferredResult;
import java.util.HashMap;
import java.util.Map;

/**
 * 只是一个实体类，用来记录信息
 */
@Component
public class DeferredResultHolder {

    // map的 key是订单号，value是DeferredResult
    private Map<String , DeferredResult> map = new HashMap<String , DeferredResult>();

    public Map<String, DeferredResult> getMap() {
        return map;
    }

    public void setMap(Map<String, DeferredResult> map) {
        this.map = map;
    }
}
