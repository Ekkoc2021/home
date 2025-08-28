package com.yang.home;

import com.yang.home.cipherkey.mapper.CipherKeyMapper;
import com.yang.home.cipherkey.pojo.CipherKeyDAO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class HomeApplicationTests {

    @Autowired
    CipherKeyMapper cipherKeyMapper;

    // 测试数据库连接
    @Test
    void databaseTest() {
        List<CipherKeyDAO> qq = cipherKeyMapper.selectByServiceName("github");
    }

}
