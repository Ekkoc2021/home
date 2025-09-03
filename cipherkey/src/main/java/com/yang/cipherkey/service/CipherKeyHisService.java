package com.yang.cipherkey.service;

import com.github.pagehelper.PageInfo;
import com.yang.cipherkey.pojo.CipherKeyHisDAO;
import com.yang.cipherkey.pojo.Result;


public interface CipherKeyHisService {

    Result<PageInfo<CipherKeyHisDAO>>  getCipherKeyHisByCipherId(int id,int pageNum, int pageSize);

    // 查询所有历史密钥
    Result<PageInfo<CipherKeyHisDAO>> listCipherKeyHis(int pageNum, int pageSize);

    // 添加历史密钥
    void addCipherKeyHis(CipherKeyHisDAO cipherKeyHisDAO);
}
