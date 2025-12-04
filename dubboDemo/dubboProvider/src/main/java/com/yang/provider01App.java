package com.yang;

import jdk.nashorn.internal.ir.annotations.Reference;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
//@ImportResource(locations = {"classpath:dubbo-provider.xml","classpath:dubbo-consumer.xml"})
@EnableDubbo
@RestController
@RequestMapping("/test")
public class provider01App {
     public static void main(String[] args) {
         // 避免启动缓存冲突
         ApplicationHome home = new ApplicationHome(provider01App.class);
         File jarFile = home.getSource();
         String dirPath = jarFile.getParentFile().toString();
         String filePath = dirPath + File.separator + ".dubbo";
         System.out.println(filePath);
         System.setProperty("dubbo.meta.cache.filePath", filePath);
         System.setProperty("dubbo.mapping.cache.filePath",filePath);

         SpringApplication.run(provider01App.class, args);
    }

    // @Reference 用dubbo包也可以
    @DubboReference
    UserService userService;
    @GetMapping("/provider")
    public void provider() {
        userService.sayHello("h");
    }
}
