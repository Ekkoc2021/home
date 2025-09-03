package com.yang.cipherkey;

import com.yang.home.common.utill.JwtUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.collections4.MapUtils;
import org.junit.jupiter.api.Test;


import java.util.*;

import static com.fasterxml.jackson.databind.type.LogicalType.Collection;


// 练习 阿帕奇的集合类型工具库
public class CollectionTest {


    @Test
    void collectionUtil() {
        List<String> listA = Arrays.asList("1", "2", "3", "4");
        List<String> listB = Arrays.asList("3", "4", "5", "6");

        List<String> listC = null;
        List<String> listD = new ArrayList<>();


        System.out.println(CollectionUtils.isEmpty(listA));
        System.out.println(CollectionUtils.isEmpty(listC)); // T
        System.out.println(CollectionUtils.isEmpty(listD)); // T

        CollectionUtils.addIgnoreNull(listD, "hello");
        CollectionUtils.addIgnoreNull(listA, null); // 不添加null元素

        System.out.println(CollectionUtils.containsAll(listA, listB));
        System.out.println(CollectionUtils.containsAny(listA, "9", "2"));

        Collection<String> subtract = CollectionUtils.subtract(listA, listB);
        subtract.forEach(e-> System.out.println(e));

        CollectionUtils.union(listA, listB).forEach(e-> System.out.println(e));
        System.out.println("===");
        CollectionUtils.intersection(listA, listB).forEach(e-> System.out.println(e));

    }

    // 用得很少
    @Test
    void mapUtil() {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("name", "John Doe");
        dataMap.put("age", 30);
        dataMap.put("isStudent", false);

        // 空值安全判断
        System.out.println("Map is empty: " + MapUtils.isEmpty(dataMap)); // false
        System.out.println("Map is not empty: " + MapUtils.isNotEmpty(dataMap)); // true

        // 类型安全获取值
        String name = MapUtils.getString(dataMap, "name");
        Integer age = MapUtils.getInteger(dataMap, "age");
        Boolean isStudent = MapUtils.getBoolean(dataMap, "isStudent");

        System.out.println("Name: " + name); // John Doe
        System.out.println("Age: " + age); // 30
        System.out.println("Is Student: " + isStudent); // false

        // 获取不存在的键（带默认值）
        String email = MapUtils.getString(dataMap, "email", "unknown@example.com");
        Integer score = MapUtils.getInteger(dataMap, "score", 100);

        System.out.println("Email: " + email); // unknown@example.com
        System.out.println("Score: " + score); // 100

        // 处理可能为null的Map
        Map<String, Object> nullMap = null;
        String value = MapUtils.getString(nullMap, "key", "default");
        System.out.println("Value from null map: " + value); // default

        value = MapUtils.getString(nullMap, "key");
        System.out.println("Value from null map: " + value); // default

        MapUtils.safeAddToMap(dataMap, "key", "value"); // value 为努力了添加 ""
        MapUtils.putAll(dataMap, null);
    }

    @Test
    //测试JWT
    void jwt() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", "yang");
        claims.put("password", "123456");
        String token = JwtUtil.getToken(claims, "123456",JwtUtil.SECOND*30);
        System.out.println(token);

        Map<String, Object> stringObjectMap = JwtUtil.validateToken(token, "123456");
        System.out.println(stringObjectMap);
    }
    @Test
    void jwt2() {

        Map<String, Object> stringObjectMap = JwtUtil.validateToken("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJwYXNzd29yZCI6IjEyMzQ1NiIsImV4cCI6MTc1NjkwNDU2MSwiaWF0IjoxNzU2OTA0NTMwNjcyLCJ1c2VybmFtZSI6InlhbmcifQ.sD_YBRJgKE2ZrG4hzuOTvVMjFkGkj1aFEWyPrBp7w2A",
                "123456");
        System.out.println(new Date(System.currentTimeMillis()));
        System.out.println(new Date(System.currentTimeMillis()));
        System.out.println(stringObjectMap);
    }


}
