package com.yang.cipherkey;

import com.yang.cipherkey.mapper.CipherKeyMapper;
import com.yang.cipherkey.pojo.CipherKeyDAO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;


@SpringBootTest(classes=CipherKeyApplictaion.class)
public class CipherKeyAppTests {

    @Autowired
    CipherKeyMapper cipherKeyMapper;

    // 测试数据库连接
    @Test
    void databaseTest() {
        List<CipherKeyDAO> qq = cipherKeyMapper.selectByServiceName("github");
    }
}
