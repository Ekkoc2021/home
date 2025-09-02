package com.yang.cipherkey.mapper;

import com.yang.cipherkey.pojo.CipherKeyHisDAO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CipherKeyHisMapper {

    void insertCipherKey(CipherKeyHisDAO cipherKeyHisDAO);
}
