package com.yang.demo1;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.Charset;

public class bufftest {
    public static void main(String[] args) {
        // 原始 ByteBuf
        ByteBuf original = Unpooled.buffer(16);
        original.writeBytes("Hello".getBytes());

        // 创建 duplicate
        ByteBuf duplicate = original.duplicate();
        // 输出原始 ByteBuf 内容
        System.out.println("Original: " + original.toString(Charset.defaultCharset()));
        //  输出 duplicate 内容
        System.out.println("Duplicate: " + duplicate.toString(Charset.defaultCharset()));
        // 打印扩容前的内存地址
        System.out.println("Before expand - original.array(): " + System.identityHashCode(original.array()));
        System.out.println("Before expand - duplicate.array(): " + System.identityHashCode(duplicate.array()));

        // 触发扩容
        original.writeBytes(" World - expanded content".getBytes());
        // 打印扩容后的内存地址
        System.out.println("Before expand - original.array(): " + System.identityHashCode(original.array()));
        System.out.println("Before expand - duplicate.array(): " + System.identityHashCode(duplicate.array()));

        // 验证: 通过 duplicate 修改数据也不会影响扩容后的 original
        duplicate.setByte(0, (byte)'h');
        // original 中的 "Hello" 不会变成 "hello"
        // 输出原始 ByteBuf 内容
        System.out.println("Original: " + original.toString(Charset.defaultCharset()));
        //  输出 duplicate 内容
        System.out.println("Duplicate: " + duplicate.toString(Charset.defaultCharset()));


    }
}
