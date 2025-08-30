package com.yang.home.async;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class AsyncTest {
    private static final Logger logger = LoggerFactory.getLogger(AsyncTest.class);
    @Async("TestPool") // 指定线程池，不指定用默认线程池
    public void async(){
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        logger.debug("异步测试 {}", Thread.currentThread().getName());
    }
}
