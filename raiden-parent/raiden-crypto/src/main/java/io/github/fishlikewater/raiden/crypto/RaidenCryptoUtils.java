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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;

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
}
