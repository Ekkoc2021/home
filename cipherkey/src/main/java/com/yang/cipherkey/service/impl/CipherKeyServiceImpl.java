package com.yang.cipherkey.service.impl;

import com.yang.cipherkey.mapper.CipherKeyMapper;
import com.yang.cipherkey.pojo.CipherKeyDAO;
import com.yang.cipherkey.pojo.CipherKeyDTO;
import com.yang.cipherkey.pojo.Result;
import com.yang.cipherkey.service.CipherKeyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;


@Service
public class CipherKeyServiceImpl implements CipherKeyService {

    Logger logger= LoggerFactory.getLogger(CipherKeyServiceImpl.class);
    @Autowired
    CipherKeyMapper cipherKeyMapper;

    @Override
    public Result getCipherKey() {
        return null;
    }

    @Override
    public Result<CipherKeyDAO> addCipherKey(CipherKeyDTO cipherKeyDTO) {
        CipherKeyDAO cipherKeyDAO = new CipherKeyDAO();
        BeanUtils.copyProperties(cipherKeyDTO,cipherKeyDAO);
        logger.debug("添加密钥:"+cipherKeyDAO);
        cipherKeyDAO.setCreateTime(new Date(System.currentTimeMillis()));
        cipherKeyDAO.setUpdateTime(new Date(System.currentTimeMillis()));
        int count=cipherKeyMapper.insertCipherKey(cipherKeyDAO);
        if(count>0){
            return Result.success(cipherKeyDAO);
        }
        return Result.error("添加密钥失败!");
    }

    @Override
    public Result modifyCipherKey(CipherKeyDTO cipherKeyDTO) {
        return null;
    }

    @Override
    public Result removeCipherKey(CipherKeyDTO cipherKeyDTO) {
        return null;
    }

    @Override
    public Result listCipherKey() {
        return null;
    }
}
