package com.yang.cipherkey.mapper;


import com.yang.cipherkey.pojo.CipherKeyDAO;
import com.yang.cipherkey.pojo.CipherKeyDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CipherKeyMapper {

    List<CipherKeyDAO> selectByServiceName(String serviceName);

    int insertCipherKey(CipherKeyDAO cipherKeyDAO);

    int updateCipherKey(CipherKeyDAO cipherKeyDAO);

    CipherKeyDAO selectById(int id);

    List<CipherKeyDAO> selectAll();

    int deleteById(int id);

    List<CipherKeyDAO> selectByCipherKeyDTO(CipherKeyDTO cipherKeyDTO);
}
