package com.yang.cipherkey.service.impl;

import com.yang.cipherkey.mapper.CipherKeyHisMapper;
import com.yang.cipherkey.mapper.CipherKeyMapper;
import com.yang.cipherkey.pojo.CipherKeyDAO;
import com.yang.cipherkey.pojo.CipherKeyDTO;
import com.yang.cipherkey.pojo.CipherKeyHisDAO;
import com.yang.cipherkey.pojo.Result;
import com.yang.cipherkey.service.CipherKeyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


@Service
public class CipherKeyServiceImpl implements CipherKeyService {

    Logger logger= LoggerFactory.getLogger(CipherKeyServiceImpl.class);
    @Autowired
    CipherKeyMapper cipherKeyMapper;

    @Autowired
    CipherKeyHisMapper cipherKeyHisMapper;

    @Override
    public Result getCipherKey() {

        return null;
    }

    @Override
    public Result<CipherKeyDAO> getCipherKeyById(int id) {
        CipherKeyDAO cipherKeyDAO = cipherKeyMapper.selectById(id);
        return Result.success(cipherKeyDAO);
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
    public Result<CipherKeyDAO> modifyCipherKey(CipherKeyDTO cipherKeyDTO) {
        CipherKeyDAO cipherKeyDAO = new CipherKeyDAO();
        BeanUtils.copyProperties(cipherKeyDTO,cipherKeyDAO);

        // 查询旧数据,然后入历史库
        CipherKeyDAO oldCipherKeyDAO = cipherKeyMapper.selectById(cipherKeyDTO.getId());
        if (oldCipherKeyDAO == null){
            return Result.error("没有该密钥");
        }
        CipherKeyHisDAO cipherKeyHisDAO = new CipherKeyHisDAO();
        BeanUtils.copyProperties(oldCipherKeyDAO,cipherKeyHisDAO);
        cipherKeyHisDAO.setCipherKeyId(cipherKeyDAO.getId());
        cipherKeyHisMapper.insertCipherKey(cipherKeyHisDAO);

        logger.debug("修改密钥:"+cipherKeyDAO);
        int count=cipherKeyMapper.updateCipherKey(cipherKeyDAO);

        if(count>0){
            return Result.success();
        }

        return Result.error("修改密钥失败!");
    }

    @Override
    public Result<CipherKeyDAO> removeCipherKey(CipherKeyDTO cipherKeyDTO) {
        return null;
    }

    @Override
    public Result<List<CipherKeyDAO>> listCipherKey() {
        return null;
    }
}
