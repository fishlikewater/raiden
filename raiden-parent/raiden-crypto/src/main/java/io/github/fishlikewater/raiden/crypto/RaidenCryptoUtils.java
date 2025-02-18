/*
 * Copyright (c) 2025 zhangxiang (fishlikewater@126.com)
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
package io.github.fishlikewater.raiden.crypto;

import io.github.fishlikewater.raiden.core.constant.CommonConstants;
import io.github.fishlikewater.raiden.crypto.exception.CryptoExceptionCheck;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKeyFactory;
import java.math.BigInteger;
import java.security.*;
import java.security.interfaces.ECPublicKey;
import java.security.spec.*;
import java.util.Arrays;

/**
 * {@code RaidenCryptoUtils}
 *
 * @author zhangxiang
 * @version 1.0.0
 * @since 2024/05/30
 */
public final class RaidenCryptoUtils {

    private static Provider provider;

    static {
        try {
            provider = ProviderFactory.createBouncyCastleProvider();
        } catch (Exception ignore) {
        }
    }

    /**
     * 创建MessageDigest
     *
     * @param algorithm 算法
     * @return MessageDigest
     */
    public static MessageDigest createMessageDigest(String algorithm) {
        MessageDigest messageDigest;
        try {
            messageDigest = (null == provider) ? MessageDigest.getInstance(algorithm) : MessageDigest.getInstance(algorithm, provider);
        } catch (NoSuchAlgorithmException e) {
            return CryptoExceptionCheck.INSTANCE.throwUnchecked(e);
        }

        return messageDigest;
    }

    /**
     * 创建Cipher
     *
     * @param algorithm 算法
     * @return Cipher
     */
    public static Cipher createCipher(String algorithm) {
        Cipher cipher;
        try {
            cipher = (null == provider) ? Cipher.getInstance(algorithm) : Cipher.getInstance(algorithm, provider);
        } catch (Exception e) {
            return CryptoExceptionCheck.INSTANCE.throwUnchecked(e);
        }

        return cipher;
    }

    /**
     * 获取KeyGenerator
     *
     * @param algorithm 算法
     * @return KeyGenerator
     */
    public static KeyGenerator getKeyGenerator(String algorithm) {
        KeyGenerator generator;
        try {
            generator = (null == provider)
                    ? KeyGenerator.getInstance(getMainAlgorithm(algorithm))
                    : KeyGenerator.getInstance(getMainAlgorithm(algorithm), provider);
        } catch (NoSuchAlgorithmException e) {
            return CryptoExceptionCheck.INSTANCE.throwUnchecked(e);
        }
        return generator;
    }

    /**
     * 获取SecretKeyFactory
     *
     * @param algorithm 算法
     * @return SecretKeyFactory
     */
    public static SecretKeyFactory getSecretKeyFactory(String algorithm) {
        SecretKeyFactory keyFactory;
        try {
            keyFactory = (null == provider)
                    ? SecretKeyFactory.getInstance(getMainAlgorithm(algorithm))
                    : SecretKeyFactory.getInstance(getMainAlgorithm(algorithm), provider);
        } catch (NoSuchAlgorithmException e) {
            return CryptoExceptionCheck.INSTANCE.throwUnchecked(e);
        }
        return keyFactory;
    }

    /**
     * 获取算法主体名称
     *
     * @param algorithm 算法
     * @return {@link KeyGenerator}
     */
    public static String getMainAlgorithm(String algorithm) {
        CryptoExceptionCheck.INSTANCE.isNotNull(algorithm, "Algorithm must be not blank!");
        final int slashIndex = algorithm.indexOf(CommonConstants.Symbol.SYMBOL_PATH);
        if (slashIndex > 0) {
            return algorithm.substring(0, slashIndex);
        }
        return algorithm;
    }

    /**
     * SM2 X509压缩格式公钥->转非压缩格式
     *
     * @param key X509压缩格式
     * @return 非压缩格式
     */
    public static byte[] sm2X509ConvertToUncompressedFormat(byte[] key) {
        // 创建X509EncodedKeySpec对象
        X509EncodedKeySpec spec = new X509EncodedKeySpec(key);

        try {
            // 使用KeyFactory生成PublicKey对象
            KeyFactory keyFactory = KeyFactory.getInstance("EC", provider);
            PublicKey publicKey = keyFactory.generatePublic(spec);
            return convertToUncompressedFormat(publicKey);
        } catch (Exception e) {
            return CryptoExceptionCheck.INSTANCE.throwUnchecked(e);
        }
    }

    /**
     * SM2非压缩格式公钥->转X509压缩格式
     *
     * @param uncompressedKey 非压缩格式
     * @return X509压缩格式
     */
    public static byte[] sm2UncompressedConvertToX509Format(byte[] uncompressedKey) {
        if (uncompressedKey == null || uncompressedKey.length != 65 || uncompressedKey[0] != 0x04) {
            throw new IllegalArgumentException("Invalid uncompressed key format");
        }

        try {
            // 提取 x 和 y 坐标
            int keySize = 32;
            byte[] xBytes = Arrays.copyOfRange(uncompressedKey, 1, 1 + keySize);
            byte[] yBytes = Arrays.copyOfRange(uncompressedKey, 1 + keySize, 1 + 2 * keySize);

            // 获取标准 JDK 的 SM2 椭圆曲线参数
            AlgorithmParameters parameters = AlgorithmParameters.getInstance("EC", provider);
            parameters.init(new ECGenParameterSpec("sm2p256v1"));
            ECParameterSpec ecSpec = parameters.getParameterSpec(ECParameterSpec.class);

            // 构造 ECPoint
            ECPoint ecPoint = new ECPoint(new BigInteger(1, xBytes), new BigInteger(1, yBytes));
            ECPublicKeySpec keySpec = new ECPublicKeySpec(ecPoint, ecSpec);

            // 生成 ECPublicKey
            KeyFactory keyFactory = KeyFactory.getInstance("EC");
            PublicKey publicKey = keyFactory.generatePublic(keySpec);

            // 转换为 X.509 格式
            return publicKey.getEncoded();
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert uncompressed key to X509 format", e);
        }
    }

    /**
     * 将SM2公钥转换为非压缩格式的字节数组。
     *
     * @param publicKey SM2公钥对象
     * @return 非压缩格式的公钥字节数组
     * @throws IllegalArgumentException 如果提供的不是有效的SM2公钥
     */
    private static byte[] convertToUncompressedFormat(PublicKey publicKey) {
        if (!(publicKey instanceof ECPublicKey ecPublicKey)) {
            return CryptoExceptionCheck.INSTANCE.throwUnchecked("The provided public key is not an EC public key.");
        }

        ECPoint w = ecPublicKey.getW();
        BigInteger x = w.getAffineX();
        BigInteger y = w.getAffineY();

        // 确保坐标编码为固定长度，这取决于所使用的椭圆曲线（对于SM2通常是256位/32字节）
        int keySize = 32;

        // 移除可能存在的前导零并填充到指定长度
        byte[] xBytes = trimAndPad(x.toByteArray(), keySize);
        byte[] yBytes = trimAndPad(y.toByteArray(), keySize);

        // 组合成非压缩格式: 04 || x || y
        byte[] uncompressed = new byte[1 + xBytes.length + yBytes.length];
        uncompressed[0] = 0x04;
        System.arraycopy(xBytes, 0, uncompressed, 1, xBytes.length);
        System.arraycopy(yBytes, 0, uncompressed, 1 + xBytes.length, yBytes.length);

        return uncompressed;
    }

    private static byte[] trimAndPad(byte[] bytes, int length) {
        // 移除前导零
        int start = bytes[0] == 0 ? 1 : 0;
        // 填充到指定长度
        byte[] result = new byte[length];
        int copyLength = Math.min(bytes.length - start, length);
        System.arraycopy(bytes, start, result, length - copyLength, copyLength);
        return result;
    }
}
