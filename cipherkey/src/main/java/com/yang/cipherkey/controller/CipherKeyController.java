package com.yang.cipherkey.controller;


import com.mysql.cj.log.Log;
import com.yang.cipherkey.pojo.CipherKeyDAO;
import com.yang.cipherkey.pojo.CipherKeyDTO;
import com.yang.cipherkey.pojo.CommonRequest;
import com.yang.cipherkey.pojo.Result;
import com.yang.cipherkey.service.CipherKeyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping()
public class CipherKeyController {

    Logger logger = LoggerFactory.getLogger(CipherKeyController.class);

    @Autowired
    private CipherKeyService cipherKeyService;
    // 功能1：添加一个密钥
    @PostMapping("/craete")
    public Result<CipherKeyDAO> createCipherKey(@RequestBody CommonRequest<CipherKeyDTO>  request ) {
        logger.debug("添加密钥 {}",request.getData());
        logger.debug("token：{}",request.getAppKey());
        Result<CipherKeyDAO> result = cipherKeyService.addCipherKey(request.getData());
        return result;
    }

    // 功能2：修改一个密钥
    @PostMapping("/update")
    public Result<CipherKeyDAO> updateCipherKey(@RequestBody CommonRequest<CipherKeyDTO>  request ) {
        Result<CipherKeyDAO> result = cipherKeyService.modifyCipherKey(request.getData());
        return result;
    }

    // 功能3：列出所有密钥
    @GetMapping("/list")
    public Result<List<CipherKeyDAO>> listCipherKey() {
        Result<List<CipherKeyDAO>> result = cipherKeyService.listCipherKey();
        logger.debug("列出密钥 {}",result.getData());
        return result;
    }

    // 功能4：删除一个密钥
    @DeleteMapping("/delete")
    public String deleteCipherKey() {
        return "delete";
    }
}
