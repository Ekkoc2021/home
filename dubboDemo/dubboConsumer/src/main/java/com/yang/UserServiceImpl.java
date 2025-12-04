package com.yang;


import org.apache.dubbo.config.annotation.DubboService;

@DubboService
public class UserServiceImpl implements UserService {
    @Override
    public void sayHello(String name) {
        System.out.println("你好 世界!");
    }
}
