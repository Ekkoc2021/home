package com.yang.cipherkey.service;

import com.yang.cipherkey.pojo.CipherKeyDAO;
import com.yang.cipherkey.pojo.CipherKeyDTO;
import com.yang.cipherkey.pojo.Result;
import org.springframework.stereotype.Service;


public interface CipherKeyService {
    Result getCipherKey();

    Result<CipherKeyDAO> addCipherKey(CipherKeyDTO cipherKeyDTO);

    Result updateCipherKey(CipherKeyDTO cipherKeyDTO);

    Result deleteCipherKey(CipherKeyDTO cipherKeyDTO);

    Result listCipherKey();

}
