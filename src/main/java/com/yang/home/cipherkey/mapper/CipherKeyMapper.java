package com.yang.home.cipherkey.mapper;

import com.yang.home.cipherkey.pojo.CipherKeyDAO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CipherKeyMapper {

    List<CipherKeyDAO> selectByServiceName(String serviceName);
}
