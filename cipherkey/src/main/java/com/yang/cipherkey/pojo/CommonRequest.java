package com.yang.cipherkey.pojo;

import lombok.Data;

@Data
public class CommonRequest<T> {
    private T data;
    private String appKey;  // 字符串用于验证请求
    private String appName; // 应用名称
    private String appVersion; // 应用版本号
}
