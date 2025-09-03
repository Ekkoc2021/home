package com.yang.cipherkey.controller;

import com.yang.cipherkey.pojo.CommonRequest;
import com.yang.cipherkey.pojo.Result;
import com.yang.cipherkey.pojo.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping()
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    //登录
    @PostMapping("/login")
    public Result<Map<String, String>> login(@RequestBody CommonRequest<UserDTO> request) {
        log.debug("用户登录 {}", request.getData());
        Map<String, String> map = new HashMap();
        map.put("token", "123456");
        return Result.success(map);
    }

}
