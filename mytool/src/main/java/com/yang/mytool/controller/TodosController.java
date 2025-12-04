package com.yang.mytool.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/todos")
public class TodosController {

    Logger logger= LoggerFactory.getLogger(TodosController.class);
    @GetMapping("/test")
    public String test(@RequestParam String name){
        logger.debug("test debug input {}",name);
        int a=99;
        int b=a+1;
        return "hello todos:"+b+name;
    }
}
