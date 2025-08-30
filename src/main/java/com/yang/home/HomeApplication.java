package com.yang.home;


import com.yang.home.async.AsyncTest;
import com.yang.home.utils.LogReqIdUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;
import java.util.concurrent.*;
import java.util.function.Supplier;


@SpringBootApplication
@EnableAsync // 允许异步机制
@RestController
@RequestMapping("/test")
public class HomeApplication {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(HomeApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(HomeApplication.class, args);
    }


    @GetMapping("/hello")
    public String test() {
        return "hello! The home project will start on here! ";
    }

    static final Logger logger = LoggerFactory.getLogger(HomeApplication.class.getName());

    @GetMapping("/log/{output}")
    public String log(@PathVariable String output) {
//        ALL < TRACE < DEBUG < INFO < WARN < ERROR < FATAL < OFF

        logger.error("error {}", output);
        logger.warn("debug {}", output);
        logger.info("info {}", output);
        logger.debug("debug {}", output);
        logger.trace("trace {}", output);

        return "日志级别测试:" + output;
    }

    public static final String REQ_ID = "REQ_ID";

    @GetMapping("/mdc")
    public String mdc(@RequestParam("output") String test) { // 将output参数值赋予test,不指定参数按照名称绑定
        String req = UUID.randomUUID().toString();
        MDC.put(REQ_ID, req); // MDC是基于threadLocal实现的,记得移除
        logger.error("MDC error {}", test);
        logger.warn("MDC debug {}", test);
        logger.info("MDC info {}", test);
        logger.debug("MDC debug {}", test);
        logger.trace("MDC trace {}", test);
        MDC.remove(REQ_ID); // MDC是基于threadLocal实现的,记得移除
        return "MDC 测试:" + req;
    }

    // LMWAYTQOJYHEYa2

    @Autowired
    @Qualifier("TestPool2")
    ThreadPoolTaskExecutor testPool;

    @Autowired
    AsyncTest asyncTest;
    // 线程池基本使用
    @GetMapping("/thread/{task}")
    public String thread(@PathVariable String task) {

        // runable
        testPool.execute(() -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            logger.info("线程池测试:{}->{}", Thread.currentThread().getName(), task);

        });


        // callable
        // 有返回值调用: 创建callable -> 放入submit -> 获取结果
        Callable<String> callable = new Callable<String>() {
            @Override
            public String call() throws Exception {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                logger.info("线程池测试:{}->{}", Thread.currentThread().getName(), task);
                return Thread.currentThread().getName();
            }
        };
        Future<String> future = testPool.submit(callable);
        // 阻塞获取结果
        try {
            String result = future.get(); // 阻塞等待
            logger.info("执行的线程名称:{}", result);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        // Supplier
        Supplier<String> supplier1 = () -> {
            logger.info("supplier 执行的线程名称:{}",Thread.currentThread().getName() );
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return Thread.currentThread().getName();
        };
        supplier1 = LogReqIdUtil.wrap(supplier1);
        CompletableFuture<String> future1 = CompletableFuture
                .supplyAsync(supplier1) // 提交异步任务,第二个参数就是指定线程池
                .thenApply(data-> "线程池测试:" + data); // 数据处理
                // thenAccept 用最终处理数据

        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> Thread.currentThread().getName());

        CompletableFuture<Void> allFutures = CompletableFuture.allOf(future1, future2);
        // 等待所有任务完成
        allFutures.join();
        // 获取各个结果
        String result1 = future1.join();
        logger.debug("执行的线程名称:{}", result1);

        // 调用异步方法
        asyncTest.async();

        String result2 = future2.join();
        logger.debug("执行的线程名称:{}", result2);

        return "线程池测试:" + task;
    }

}
