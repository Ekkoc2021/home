package com.yang.cipherkey.pojo;

import lombok.Data;

@Data
public class CommonRequest<T> {
    private T data;
    private String token;  // token字符串用于验证请求
    
    // getter 和 setter 方法
    public T getData() {
        return data;
    }
    
    public void setData(T data) {
        this.data = data;
    }

}
