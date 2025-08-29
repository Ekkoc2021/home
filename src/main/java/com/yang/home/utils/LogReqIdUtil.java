package com.yang.home.utils;

import org.slf4j.MDC;

import java.util.UUID;

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
}
