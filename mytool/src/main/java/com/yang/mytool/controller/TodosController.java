package com.yang.mytool.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/todos")
public class TodosController {

    @GetMapping("/test")
    public String test(@RequestParam String name){
        int a=99;
        int b=a+1;
        return "hello todos:"+b+name;
    }
}
