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

import io.github.fishlikewater.raiden.core.ObjectUtils;
import io.github.fishlikewater.raiden.core.RandomUtils;
import io.github.fishlikewater.raiden.core.StringUtils;
import io.github.fishlikewater.raiden.crypto.RaidenCryptoUtils;
import io.github.fishlikewater.raiden.crypto.exception.CryptoExceptionCheck;
import lombok.Getter;
import lombok.Setter;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEParameterSpec;
import java.security.spec.AlgorithmParameterSpec;

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

    @Setter
    private AlgorithmParameterSpec params;

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
                iv = RandomUtils.randomNumberAndAlphabet(8).getBytes();
            }
            paramsSpec = new PBEParameterSpec(iv, 100);
        } else if (StringUtils.startWithIgnoreCase(algorithm, SymmetricUtils.AES)) {
            if (null != iv) {
                paramsSpec = new IvParameterSpec(iv);
            }
        }

        this.params = paramsSpec;
    }

    // ---------------------------------------------------------------- crypto
}
