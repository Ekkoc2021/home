package com.yang.cipherkey.pojo;

import lombok.Data;

@Data
public class CipherKeyDTO {
    private int id;
    private String serviceName;
    private String username;
    private String pwd;
    private String notes;
    private String category;
}
