package com.yang;

import org.apache.dubbo.config.annotation.DubboService;

@DubboService
public interface UserService {

    void sayHello(String name);
}
