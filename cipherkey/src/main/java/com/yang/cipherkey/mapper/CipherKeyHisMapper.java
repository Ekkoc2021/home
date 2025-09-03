package com.yang.cipherkey.mapper;

import com.yang.cipherkey.pojo.CipherKeyHisDAO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CipherKeyHisMapper {

    void insertCipherKey(CipherKeyHisDAO cipherKeyHisDAO);

    List<CipherKeyHisDAO> getCipherKeyHisByCipherId(int id);

    List<CipherKeyHisDAO> listCipherKeyHis();
}
