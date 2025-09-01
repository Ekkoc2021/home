package com.yang.cipherkey.pojo;

import lombok.Data;

@Data
public class Result<T> {
    private Integer code;  // 状态码
    private String message; // 返回信息
    private T data;   // 返回数据，使用泛型

    // 成功返回结果（无数据）
    public static <T> Result<T> success() {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMessage("succes");
        return result;
    }

    // 成功返回结果（带数据）
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMessage("succes");
        result.setData(data);
        return result;
    }

    // 失败返回结果
    public static <T> Result<T> error(String message) {
        Result<T> result = new Result<>();
        result.setCode(500);
        result.setMessage(message);
        return result;
    }

    // 失败返回结果，自定义状态码
    public static <T> Result<T> error(Integer code, String message) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }
}
