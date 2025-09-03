package com.yang.cipherkey.pojo;

import lombok.Data;

@Data
public class PageRequest {
    /**
     * 当前页码，默认第1页
     */
    private Integer pageNum = 1;
    
    /**
     * 每页大小，默认10条
     */
    private Integer pageSize = 10;
    
    /**
     * 排序字段
     */
    private String orderBy;
    
    /**
     * 排序方式：asc/desc
     */
    private String orderType;
    
    // 构造函数
    public PageRequest() {}
    
    public PageRequest(Integer pageNum, Integer pageSize) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }
    
    public PageRequest(Integer pageNum, Integer pageSize, String orderBy, String orderType) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.orderBy = orderBy;
        this.orderType = orderType;
    }

}
