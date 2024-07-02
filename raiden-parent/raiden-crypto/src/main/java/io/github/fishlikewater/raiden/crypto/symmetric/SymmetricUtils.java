/*
 * Copyright (c) 2024 zhangxiang (fishlikewater@126.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.fishlikewater.raiden.crypto.symmetric;

import io.github.fishlikewater.raiden.core.RandomUtils;
import io.github.fishlikewater.raiden.core.StringUtils;
import io.github.fishlikewater.raiden.crypto.RaidenCryptoUtils;
import io.github.fishlikewater.raiden.crypto.exception.CryptoExceptionCheck;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

/**
 * {@code SymmetricUtils}
 * 对称加密算法工具类
 *
 * @author zhangxiang
 * @version 1.0.3
 * @since 2024/07/02
 */
@SuppressWarnings("all")
public class SymmetricUtils {

    public static final String DES = "DES";

    public static final String AES = "AES";

    public static final String PBE = "PBE";

    public static final String DES_ADE = "DESede";

    // ---------------------------------------------------------------- SecretKey

    /**
     * 生成密钥
     *
     * @param algorithm 算法
     * @return 密钥
     */
    public static SecretKey generateKey(String algorithm) {
        return generateKey(algorithm, -1);
    }

    /**
     * 生成密钥
     *
     * @param algorithm 算法
     * @param keySize   密钥长度
     * @return 密钥
     */
    public static SecretKey generateKey(String algorithm, int keySize) {
        return generateKey(algorithm, keySize, null);
    }

    /**
     * 生成密钥
     *
     * @param algorithm 算法
     * @param keySize   密钥长度
     * @param random    随机数
     * @return 密钥
     */
    public static SecretKey generateKey(String algorithm, int keySize, SecureRandom random) {
        algorithm = RaidenCryptoUtils.getMainAlgorithm(algorithm);

        final KeyGenerator keyGenerator = RaidenCryptoUtils.getKeyGenerator(algorithm);
        if (keySize <= 0 && SymmetricAlgorithm.AES.getValue().equals(algorithm)) {
            // 对于AES的密钥，除非指定，否则强制使用128位
            keySize = 128;
        }

        if (keySize > 0) {
            if (null == random) {
                keyGenerator.init(keySize);
            } else {
                keyGenerator.init(keySize, random);
            }
        }
        return keyGenerator.generateKey();
    }

    /**
     * 生成密钥
     *
     * @param algorithm 算法
     * @param key       密钥
     * @return 密钥
     */
    public static SecretKey generateKey(String algorithm, byte[] key) {
        CryptoExceptionCheck.INSTANCE.isNotNull(algorithm, "Algorithm is null!");
        SecretKey secretKey;
        if (algorithm.startsWith(PBE)) {
            // PBE密钥
            secretKey = generatePBEKey(algorithm, (null == key) ? null : StringUtils.utf8Str(key).toCharArray());
        } else if (algorithm.startsWith(DES)) {
            // DES密钥
            secretKey = generateDESKey(algorithm, key);
        } else {
            // 其它算法密钥
            secretKey = (null == key) ? generateKey(algorithm) : new SecretKeySpec(key, algorithm);
        }
        return secretKey;
    }

    /**
     * 生成 {@link SecretKey}
     *
     * @param algorithm DES算法，包括DES、DESede等
     * @param key       密钥
     * @return {@link SecretKey}
     */
    public static SecretKey generateDESKey(String algorithm, byte[] key) {
        if (StringUtils.isBlank(algorithm) || !algorithm.startsWith(DES)) {
            return CryptoExceptionCheck.INSTANCE.throwUnchecked("Algorithm [{}] is not a DES algorithm!", algorithm);
        }

        SecretKey secretKey;
        if (null == key) {
            secretKey = generateKey(algorithm);
        } else {
            KeySpec keySpec;
            try {
                if (algorithm.startsWith(DES_ADE)) {
                    // DESede兼容
                    keySpec = new DESedeKeySpec(key);
                } else {
                    keySpec = new DESKeySpec(key);
                }
            } catch (InvalidKeyException e) {
                return CryptoExceptionCheck.INSTANCE.throwUnchecked(e);
            }
            secretKey = generateKey(algorithm, keySpec);
        }
        return secretKey;
    }

    /**
     * 生成PBE {@link SecretKey}
     *
     * @param algorithm PBE算法，包括：PBEWithMD5AndDES、PBEWithSHA1AndDESede、PBEWithSHA1AndRC2_40等
     * @param key       密钥
     * @return {@link SecretKey}
     */
    public static SecretKey generatePBEKey(String algorithm, char[] key) {
        if (StringUtils.isBlank(algorithm) || !algorithm.startsWith(PBE)) {
            return CryptoExceptionCheck.INSTANCE.throwUnchecked("Algorithm [{}] is not a PBE algorithm!", algorithm);
        }

        if (null == key) {
            key = RandomUtils.randomNumberAndAlphabet(32).toCharArray();
        }
        PBEKeySpec keySpec = new PBEKeySpec(key);
        return generateKey(algorithm, keySpec);
    }

    /**
     * 生成 {@link SecretKey}，仅用于对称加密和摘要算法
     *
     * @param algorithm 算法
     * @param keySpec   {@link KeySpec}
     * @return {@link SecretKey}
     */
    public static SecretKey generateKey(String algorithm, KeySpec keySpec) {
        final SecretKeyFactory keyFactory = RaidenCryptoUtils.getSecretKeyFactory(algorithm);
        try {
            return keyFactory.generateSecret(keySpec);
        } catch (InvalidKeySpecException e) {
            return CryptoExceptionCheck.INSTANCE.throwUnchecked(e);
        }
    }
}
