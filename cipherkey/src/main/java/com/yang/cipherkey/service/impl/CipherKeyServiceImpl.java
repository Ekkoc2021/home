package com.yang.cipherkey.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yang.cipherkey.mapper.CipherKeyHisMapper;
import com.yang.cipherkey.mapper.CipherKeyMapper;
import com.yang.cipherkey.pojo.CipherKeyDAO;
import com.yang.cipherkey.pojo.CipherKeyDTO;
import com.yang.cipherkey.pojo.CipherKeyHisDAO;
import com.yang.cipherkey.pojo.Result;
import com.yang.cipherkey.service.CipherKeyHisService;
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
    CipherKeyHisService cipherKeyHisService;

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
//        CipherKeyDAO oldCipherKeyDAO = cipherKeyMapper.selectById(cipherKeyDTO.getId());
        // 修改为根据service_name查询
        List<CipherKeyDAO> oldCipherKeyDAOs = cipherKeyMapper.selectByServiceName(cipherKeyDTO.getServiceName());
        // service_name只有一条数据
        if (oldCipherKeyDAOs.size() ==0){
            return Result.error("没有该密钥");
        }
        CipherKeyDAO oldCipherKeyDAO = oldCipherKeyDAOs.get(0);
        CipherKeyHisDAO cipherKeyHisDAO = new CipherKeyHisDAO();
        BeanUtils.copyProperties(oldCipherKeyDAO,cipherKeyHisDAO);
        cipherKeyHisDAO.setCipherKeyId(oldCipherKeyDAO.getId());

        cipherKeyDAO.setUpdateTime(new Date(System.currentTimeMillis()));
        logger.debug("修改密钥:"+cipherKeyDAO);
        int count=cipherKeyMapper.updateCipherKeyByServiceName(cipherKeyDAO);

        if(count>0){
            cipherKeyHisService.addCipherKeyHis(cipherKeyHisDAO);
            return Result.success();
        }
        return Result.error("修改密钥失败!");
    }

    @Override
    public Result<CipherKeyDAO> removeCipherKey(CipherKeyDTO cipherKeyDTO) {
        logger.debug("删除密钥:"+cipherKeyDTO);
        List<CipherKeyDAO> cipherKeyDAOs = cipherKeyMapper.selectByServiceName(cipherKeyDTO.getServiceName());
        if (cipherKeyDAOs==null || cipherKeyDAOs.size()==0){
            return Result.error("没有该密钥");
        }
        CipherKeyDAO cipherKeyDAO = cipherKeyDAOs.get(0);
        CipherKeyHisDAO cipherKeyHisDAO = new CipherKeyHisDAO();
        BeanUtils.copyProperties(cipherKeyDAO,cipherKeyHisDAO);
        cipherKeyHisDAO.setCipherKeyId(cipherKeyDAO.getId());
        int count=cipherKeyMapper.deleteById(cipherKeyDAO.getId());

        if(count==0){
           return Result.error("删除密钥失败!");
        }
        cipherKeyHisService.addCipherKeyHis(cipherKeyHisDAO);
        return Result.success(cipherKeyDAO);
    }

    @Override
    public Result<PageInfo<CipherKeyDAO>> listCipherKey(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<CipherKeyDAO> cipherKeyDAOS = cipherKeyMapper.selectAll();

        //封装pageinfo
        PageInfo<CipherKeyDAO> pageInfo = new PageInfo<>(cipherKeyDAOS);
        return Result.success(pageInfo);
    }

    @Override
    public  Result<PageInfo<CipherKeyDAO>> listCipherKeyByCipherKeyDTO(CipherKeyDTO data, int pageNum, int pageSize) {

        PageHelper.startPage( pageNum, pageSize);
        List<CipherKeyDAO> cipherKeyDAOS = cipherKeyMapper.selectByCipherKeyDTO(data);
        PageInfo<CipherKeyDAO> pageInfo = new PageInfo<>(cipherKeyDAOS);
        return Result.success(pageInfo);
    }
}
