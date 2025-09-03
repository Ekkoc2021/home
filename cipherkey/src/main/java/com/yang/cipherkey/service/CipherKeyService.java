package com.yang.cipherkey.service;

import com.github.pagehelper.PageInfo;
import com.yang.cipherkey.pojo.CipherKeyDAO;
import com.yang.cipherkey.pojo.CipherKeyDTO;
import com.yang.cipherkey.pojo.Result;
import org.springframework.stereotype.Service;

import java.util.List;


public interface CipherKeyService {
    Result getCipherKey();

    Result<CipherKeyDAO> getCipherKeyById(int id);

    Result<CipherKeyDAO> addCipherKey(CipherKeyDTO cipherKeyDTO);

    Result<CipherKeyDAO> modifyCipherKey(CipherKeyDTO cipherKeyDTO);

    Result<CipherKeyDAO> removeCipherKey(CipherKeyDTO cipherKeyDTO);

    Result<PageInfo<CipherKeyDAO>>  listCipherKey(int pageNum, int pageSize);

    Result<PageInfo<CipherKeyDAO>>  listCipherKeyByCipherKeyDTO(CipherKeyDTO data, int pageNum, int pageSize);
}
