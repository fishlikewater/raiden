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
package io.github.fishlikewater.raiden.crypto;

import io.github.fishlikewater.raiden.crypto.exception.CryptoExceptionCheck;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;

/**
 * {@code Digest}
 * 摘要算法
 *
 * @author zhangxiang
 * @version 1.0.2
 * @since 2024/06/11
 */
public class Digest {

    private MessageDigest digest;
    /**
     * 算法
     */
    private String algorithm;
    /**
     * 盐
     */
    private String salt;
    /**
     * 加盐位置，即将盐值字符串放置在数据的index数，默认0
     */
    protected int saltPosition;
    /**
     * 散列次数
     */
    protected int digestCount;

    /**
     * 构造
     *
     * @param algorithm 算法枚举
     */
    public Digest(DigestAlgorithm algorithm) {
        this(algorithm.getValue());
    }

    /**
     * 构造
     *
     * @param algorithm 算法枚举
     */
    public Digest(String algorithm) {
        this(algorithm, null);
    }

    /**
     * 构造
     *
     * @param algorithm 算法
     * @param provider  算法提供者，null表示JDK默认，可以引入Bouncy Castle等来提供更多算法支持
     */
    public Digest(DigestAlgorithm algorithm, Provider provider) {
        init(algorithm.getValue(), provider);
    }

    /**
     * 构造
     *
     * @param algorithm 算法
     * @param provider  算法提供者，null表示JDK默认，可以引入Bouncy Castle等来提供更多算法支持
     */
    public Digest(String algorithm, Provider provider) {
        init(algorithm, provider);
    }

    /**
     * 构造
     *
     * @param messageDigest {@link MessageDigest}
     */
    public Digest(final MessageDigest messageDigest) {
        this.digest = messageDigest;
    }

    /**
     * 初始化
     *
     * @param algorithm 算法
     * @param provider  算法提供者，null表示JDK默认，可以引入Bouncy Castle等来提供更多算法支持
     * @return Digester
     * @throws io.github.fishlikewater.raiden.core.exception.RaidenException Cause by IOException
     */
    public Digest init(String algorithm, Provider provider) {
        if (null == provider) {
            this.digest = RaidenCryptoUtils.createMessageDigest(algorithm);
        } else {
            try {
                this.digest = MessageDigest.getInstance(algorithm, provider);
            } catch (NoSuchAlgorithmException e) {
                CryptoExceptionCheck.INSTANCE.throwUnchecked(e);
            }
        }
        return this;
    }

}
