package com.yang.home.utils;

import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.function.Supplier;

public final class LogReqIdUtil {
    private LogReqIdUtil() {

    }

    // 序列号key=>修改需要同步改配置文件
    private static final String REQ_ID = "REQ_ID";

    // 生成序号的
    private static String generateId() {
        return UUID.randomUUID().toString();
    }

    // 设置MDC
    public static void setMDCLogReqId() {
        String id = generateId();
        MDC.put(REQ_ID, id);
    }

    public static void removeMDCLogReqId() {
        MDC.remove(REQ_ID);
    }

    // 包装新的Supplier
    public static <T> Supplier<T> wrap(final Supplier<T> supplier) {
        // 获取当前线程的 MDC 上下文
        Map<String, String> contextMap = MDC.getCopyOfContextMap();
        return () -> {
            try {
                // 设置 MDC 上下文到新线程
                if (contextMap != null) {
                    MDC.setContextMap(contextMap);
                }
                return supplier.get();
            } finally {
                // 清理新线程的 MDC 上下文
                MDC.clear();
            }
        };
    }

    // 包装新的callable
    public static <T> Callable<T> wrap(final Callable<T> callable) {
        // 获取当前线程的 MDC 上下文
        Map<String, String> contextMap = MDC.getCopyOfContextMap();
        return () -> {
            try {
                // 设置 MDC 上下文到新线程
                if (contextMap != null) {
                    MDC.setContextMap(contextMap);
                }
                return callable.call();
            } finally {
                // 清理新线程的 MDC 上下文
                MDC.clear();
            }
        };
    }

    // 包装新的runable
    public static  Runnable wrap(final Runnable runnable) {
        // 获取当前线程的 MDC 上下文
        Map<String, String> contextMap = MDC.getCopyOfContextMap();
        return () -> {
            try {
                // 设置 MDC 上下文到新线程
                if (contextMap != null) {
                    MDC.setContextMap(contextMap);
                }
                runnable.run();
            } finally {
                // 清理新线程的 MDC 上下文
                MDC.clear();
            }
        };
    }

    // 线程池任务装饰器 ==> 只能解决callable和runable
    public static class MdcTaskDecorator implements TaskDecorator {
        @Override
        public Runnable decorate(Runnable runnable) {
            return wrap(runnable);
        }

    }
}
