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


import io.github.fishlikewater.raiden.core.CollectionUtils;
import io.github.fishlikewater.raiden.core.ObjectUtils;
import io.github.fishlikewater.raiden.core.RandomUtils;
import io.github.fishlikewater.raiden.core.StringUtils;
import io.github.fishlikewater.raiden.crypto.Padding;
import io.github.fishlikewater.raiden.crypto.RaidenCryptoUtils;
import io.github.fishlikewater.raiden.crypto.exception.CryptoExceptionCheck;
import lombok.Getter;
import lombok.Setter;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEParameterSpec;
import java.security.spec.AlgorithmParameterSpec;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * {@code SymmetricCrypto}
 * 对称加密
 *
 * @author zhangxiang
 * @version 1.0.3
 * @since 2024/07/02
 */
@Getter
public class SymmetricCrypto {

    private Cipher cipher;

    private SecretKey secretKey;

    private boolean isZeroPadding;

    @Setter
    private AlgorithmParameterSpec params;

    private final Lock lock = new ReentrantLock();

    public static SymmetricCrypto create(String algorithm, SecretKey key) {
        return new SymmetricCrypto(algorithm, key, null);
    }

    // ---------------------------------------------------------------- constructor

    public SymmetricCrypto(Cipher cipher, SecretKey secretKey) {
        this.cipher = cipher;
        this.secretKey = secretKey;
    }

    /**
     * 构造
     *
     * @param algorithm  算法
     * @param key        密钥
     * @param paramsSpec 算法参数，例如加盐等
     */
    public SymmetricCrypto(String algorithm, SecretKey key, AlgorithmParameterSpec paramsSpec) {
        init(algorithm, key);
        initParams(algorithm, paramsSpec);
    }

    // ---------------------------------------------------------------- init

    /**
     * 初始化
     *
     * @param algorithm 算法
     * @param key       密钥
     */
    public void init(String algorithm, SecretKey key) {
        CryptoExceptionCheck.INSTANCE.isNotNull(algorithm, "algorithm must be not null !");
        this.secretKey = key;
        // jdk 不支持ZeroPadding 将其转换为NoPadding 并设置标志自定义处理
        if (algorithm.contains(Padding.ZeroPadding.name())) {
            algorithm = StringUtils.replace(algorithm, Padding.ZeroPadding.name(), Padding.NoPadding.name());
            this.isZeroPadding = true;
        }
        this.cipher = RaidenCryptoUtils.createCipher(algorithm);
    }

    /**
     * 初始化参数
     *
     * @param algorithm  算法
     * @param paramsSpec 算法参数，例如加盐等
     */
    public void initParams(String algorithm, AlgorithmParameterSpec paramsSpec) {
        if (ObjectUtils.isNotNullOrEmpty(paramsSpec)) {
            this.params = paramsSpec;
        }

        byte[] iv = this.cipher.getIV();
        if (StringUtils.startWithIgnoreCase(algorithm, SymmetricUtils.PBE)) {
            // 对于PBE算法使用随机数加盐
            if (null == iv) {
                iv = RandomUtils.randomNumberAndAlphabet(16).getBytes();
            }
            paramsSpec = new PBEParameterSpec(iv, 128);
        } else if (StringUtils.startWithIgnoreCase(algorithm, SymmetricUtils.AES)) {
            if (null != iv) {
                paramsSpec = new IvParameterSpec(iv);
            }
        }

        this.params = paramsSpec;
    }

    // ---------------------------------------------------------------- crypto

    /**
     * 加密
     *
     * @param data 数据
     * @return 加密后的数据
     */
    public byte[] encrypt(byte[] data) {
        this.lock.lock();
        try {
            this.initMode(Cipher.ENCRYPT_MODE);
            return this.cipher.doFinal(this.populateZero(data, cipher.getBlockSize()));
        } catch (Exception e) {
            return CryptoExceptionCheck.INSTANCE.throwUnchecked(e);
        } finally {
            this.lock.unlock();
        }
    }

    public byte[] decrypt(byte[] bytes) {
        final int blockSize;
        final byte[] decryptData;

        this.lock.lock();
        try {
            this.initMode(Cipher.DECRYPT_MODE);
            blockSize = this.cipher.getBlockSize();
            decryptData = this.cipher.doFinal(bytes);
        } catch (Exception e) {
            return CryptoExceptionCheck.INSTANCE.throwUnchecked(e);
        } finally {
            this.lock.unlock();
        }

        return this.delZero(decryptData, blockSize);
    }

    // ---------------------------------------------------------------- private

    /**
     * <a href="https://blog.csdn.net/OrangeJack/article/details/82913804">...</a>
     * 填充数据
     *
     * @param data      数据
     * @param blockSize 块大小
     * @return 填充后的数据
     */
    private byte[] populateZero(byte[] data, int blockSize) {
        if (this.isZeroPadding) {
            final int length = data.length;
            // 按照块拆分后的数据中多余的数据
            final int remainLength = length % blockSize;
            if (remainLength > 0) {
                // 新长度为blockSize的整数倍，多余部分填充0
                return CollectionUtils.resize(data, length + blockSize - remainLength);
            }
        }
        return data;
    }

    /**
     * 移除填充数据
     *
     * @param data      数据
     * @param blockSize 块大小
     * @return 移除填充后的数据
     */
    private byte[] delZero(byte[] data, int blockSize) {
        if (this.isZeroPadding && blockSize > 0) {
            final int length = data.length;
            final int remainLength = length % blockSize;
            if (remainLength == 0) {
                // 解码后的数据正好是块大小的整数倍，说明可能存在补0的情况，去掉末尾所有的0
                int i = length - 1;
                while (i >= 0 && 0 == data[i]) {
                    i--;
                }
                return CollectionUtils.resize(data, i + 1);
            }
        }
        return data;
    }

    /**
     * 设置模式
     *
     * @param mode 模式
     */
    private void setMode(int mode) {
        this.lock.lock();
        try {
            initMode(mode);
        } finally {
            this.lock.unlock();
        }
    }

    /**
     * 初始化模式
     *
     * @param mode 模式
     */
    private void initMode(int mode) {
        try {
            cipher.init(mode, secretKey, params);
        } catch (Exception e) {
            CryptoExceptionCheck.INSTANCE.throwUnchecked(e);
        }
    }
}
