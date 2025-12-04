package com.yang;


import org.apache.dubbo.config.annotation.DubboService;

// @Service 也可以,使用dubbo提供的即可
@DubboService
public class ProviderServiceImpl implements ProviderService {

    public String sayHello(String name) {
        System.out.println("Hello " + name);
        return "dubbo hello world";
    }
}
