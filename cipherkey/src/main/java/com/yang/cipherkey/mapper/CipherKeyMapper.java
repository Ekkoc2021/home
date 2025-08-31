package com.yang.cipherkey.mapper;


import com.yang.cipherkey.pojo.CipherKeyDAO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CipherKeyMapper {

    List<CipherKeyDAO> selectByServiceName(String serviceName);
}
