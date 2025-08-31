package com.yang.cipherkey.pojo;

import lombok.Data;

import java.sql.Date;

@Data
public class CipherKeyDAO {
    private int id;
    private String serviceName;
    private String username;
    private String pwd;
    private String notes;
    private String category;
    private Date updateTime;
    private Date createTime;
}
