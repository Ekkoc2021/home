package com.yang.home;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@SpringBootApplication
@RestController
@RequestMapping("/test")
public class HomeApplication {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(HomeApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(HomeApplication.class, args);
    }



    @GetMapping("/hello")
    public String test(){

        return "hello! The home project will start on here! ";
    }

    static final Logger logger = LoggerFactory.getLogger(HomeApplication.class.getName());
    @GetMapping("/log/{output}")
    public String log(@PathVariable String output){
//        ALL < TRACE < DEBUG < INFO < WARN < ERROR < FATAL < OFF

        logger.error("error {}",output);
        logger.warn("debug {}",output);
        logger.info("info {}",output);
        logger.debug("debug {}",output);
        logger.trace("trace {}",output) ;

        return "日志级别测试:"+output;
    }
    // LMWAYTQOJYHEYa2

}
