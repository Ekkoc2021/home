package com.yang.cipherkey;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.crypto.generators.PKCS5S2ParametersGenerator;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.modes.GCMBlockCipher;
import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.params.AEADParameters;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Arrays;
import java.util.Base64;

public class AesGcmCompat {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    private static final int SALT_SIZE = 16;
    private static final int NONCE_SIZE = 12;
    private static final int TAG_SIZE = 16; // 128 bits

    public static String decrypt(String encryptedBase64, String password) throws Exception {
        byte[] data = Base64.getDecoder().decode(encryptedBase64);

        if (data.length < SALT_SIZE + NONCE_SIZE + TAG_SIZE) {
            throw new IllegalArgumentException("Data too short");
        }

        byte[] salt = Arrays.copyOfRange(data, 0, SALT_SIZE);
        byte[] nonce = Arrays.copyOfRange(data, SALT_SIZE, SALT_SIZE + NONCE_SIZE);
        byte[] tag = Arrays.copyOfRange(data, SALT_SIZE + NONCE_SIZE, SALT_SIZE + NONCE_SIZE + TAG_SIZE);
        byte[] ciphertext = Arrays.copyOfRange(data, SALT_SIZE + NONCE_SIZE + TAG_SIZE, data.length);

        // 派生密钥
        byte[] key = deriveKeyBc(password, salt);

        // 初始化 GCM 解密器
        GCMBlockCipher gcm = new GCMBlockCipher(new AESEngine());
        AEADParameters parameters = new AEADParameters(new KeyParameter(key), 128, nonce, null);
        gcm.init(false, parameters); // false = decrypt

        // ✅ 关键：把 tag 附加到 ciphertext 末尾，组成 "ciphertextWithTag"
        byte[] ciphertextWithTag = new byte[ciphertext.length + TAG_SIZE];
        System.arraycopy(ciphertext, 0, ciphertextWithTag, 0, ciphertext.length);
        System.arraycopy(tag, 0, ciphertextWithTag, ciphertext.length, TAG_SIZE);

        // 计算输出缓冲区大小
        byte[] plaintext = new byte[gcm.getOutputSize(ciphertextWithTag.length)];

        // 处理数据
        int len = gcm.processBytes(ciphertextWithTag, 0, ciphertextWithTag.length, plaintext, 0);

        // 完成解密（内部会验证 tag）
        len += gcm.doFinal(plaintext, len);

        // 返回结果
        return new String(plaintext, 0, len, StandardCharsets.UTF_8);
    }

    public static String encrypt(String plaintext, String password) throws Exception {
        // 生成盐
        byte[] salt = new byte[SALT_SIZE];
        new SecureRandom().nextBytes(salt);

        // 生成 nonce
        byte[] nonce = new byte[NONCE_SIZE];
        new SecureRandom().nextBytes(nonce);

        // 派生密钥
        byte[] key = deriveKeyBc(password, salt);

        // 初始化 GCM 加密器
        GCMBlockCipher gcm = new GCMBlockCipher(new AESEngine());
        AEADParameters parameters = new AEADParameters(new KeyParameter(key), 128, nonce, null);
        gcm.init(true, parameters); // true = encrypt

        // 加密
        byte[] plaintextBytes = plaintext.getBytes(StandardCharsets.UTF_8);
        byte[] ciphertext = new byte[gcm.getOutputSize(plaintextBytes.length)];
        int len = gcm.processBytes(plaintextBytes, 0, plaintextBytes.length, ciphertext, 0);
        len += gcm.doFinal(ciphertext, len);

        // 获取 tag（在 ciphertext 的最后 TAG_SIZE 字节）
        byte[] tag = new byte[TAG_SIZE];
        System.arraycopy(ciphertext, len - TAG_SIZE, tag, 0, TAG_SIZE);

        // 准备最终数据（盐 + nonce + tag + ciphertext（不包含tag部分））
        byte[] ciphertextWithoutTag = Arrays.copyOf(ciphertext, len - TAG_SIZE);
        byte[] result = new byte[SALT_SIZE + NONCE_SIZE + TAG_SIZE + ciphertextWithoutTag.length];

        System.arraycopy(salt, 0, result, 0, SALT_SIZE);
        System.arraycopy(nonce, 0, result, SALT_SIZE, NONCE_SIZE);
        System.arraycopy(tag, 0, result, SALT_SIZE + NONCE_SIZE, TAG_SIZE);
        System.arraycopy(ciphertextWithoutTag, 0, result, SALT_SIZE + NONCE_SIZE + TAG_SIZE, ciphertextWithoutTag.length);

        // Base64 编码
        return Base64.getEncoder().encodeToString(result);
    }
    private static byte[] deriveKeyBc(String password, byte[] salt) {
        PKCS5S2ParametersGenerator gen = new PKCS5S2ParametersGenerator(new SHA256Digest());
        gen.init(password.getBytes(StandardCharsets.UTF_8), salt, 100000);
        return ((KeyParameter) gen.generateDerivedParameters(256)).getKey();
    }

}