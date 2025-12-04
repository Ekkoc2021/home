package com.yang.cipherkey;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class CipherKeyApplictaion {
    public static void main(String[] args) {
        SpringApplication.run(CipherKeyApplictaion.class, args);
    }

}
