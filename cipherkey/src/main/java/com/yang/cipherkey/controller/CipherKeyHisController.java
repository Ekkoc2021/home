package com.yang.cipherkey.controller;

import com.github.pagehelper.PageInfo;
import com.yang.cipherkey.pojo.CipherKeyHisDAO;
import com.yang.cipherkey.pojo.Result;
import com.yang.cipherkey.service.CipherKeyHisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/his")
public class CipherKeyHisController {

    @Autowired
    private CipherKeyHisService cipherKeyHisService;
    // 根据id查询历史密钥
    @GetMapping("/getById/{cipherId}/{pageNum}/{pageSize}")
    public Result<PageInfo<CipherKeyHisDAO>> getById(@PathVariable int cipherId,@PathVariable int pageNum,@PathVariable int pageSize) {
        return cipherKeyHisService.getCipherKeyHisByCipherId(cipherId,pageNum,pageSize);
    }

    // 查询所有历史密钥
    @GetMapping("/list/{pageNum}/{pageSize}")
    public Result<PageInfo<CipherKeyHisDAO>> list( @PathVariable int pageNum,@PathVariable int pageSize) {
        return cipherKeyHisService.listCipherKeyHis(pageNum,pageSize);
    }
}
