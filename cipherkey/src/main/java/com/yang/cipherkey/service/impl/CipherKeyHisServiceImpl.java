package com.yang.cipherkey.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yang.cipherkey.mapper.CipherKeyHisMapper;
import com.yang.cipherkey.pojo.CipherKeyHisDAO;
import com.yang.cipherkey.pojo.Result;
import com.yang.cipherkey.service.CipherKeyHisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CipherKeyHisServiceImpl implements CipherKeyHisService {
    @Autowired
    private CipherKeyHisMapper cipherKeyHisMapper;
    @Override
    public Result<PageInfo<CipherKeyHisDAO>> getCipherKeyHisByCipherId(int id,int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        PageInfo<CipherKeyHisDAO> pageInfo = new PageInfo<>(cipherKeyHisMapper.getCipherKeyHisByCipherId(id));

        return Result.success(pageInfo);
    }

    @Override
    public Result<PageInfo<CipherKeyHisDAO>> listCipherKeyHis(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        PageInfo<CipherKeyHisDAO> pageInfo = new PageInfo<>(cipherKeyHisMapper.listCipherKeyHis());
        return Result.success(pageInfo);
    }

    @Override
    public void addCipherKeyHis(CipherKeyHisDAO cipherKeyHisDAO) {
        cipherKeyHisMapper.insertCipherKey(cipherKeyHisDAO);
    }
}
