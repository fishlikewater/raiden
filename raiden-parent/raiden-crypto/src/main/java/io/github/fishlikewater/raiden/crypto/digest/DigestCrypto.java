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
package io.github.fishlikewater.raiden.crypto.digest;

import io.github.fishlikewater.raiden.core.FileUtils;
import io.github.fishlikewater.raiden.core.ObjectUtils;
import io.github.fishlikewater.raiden.core.StringUtils;
import io.github.fishlikewater.raiden.core.constant.CommonConstants;
import io.github.fishlikewater.raiden.crypto.Hex;
import io.github.fishlikewater.raiden.crypto.RaidenCryptoUtils;
import io.github.fishlikewater.raiden.crypto.exception.CryptoExceptionCheck;
import io.github.fishlikewater.raiden.crypto.exception.RaidenCryptoException;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;

/**
 * {@code Digest}
 * 摘要算法 参考hutool 实现方式
 *
 * @author zhangxiang
 * @version 1.0.2
 * @since 2024/06/11
 */
@SuppressWarnings("unused")
public class DigestCrypto {

    @Getter
    private MessageDigest digest;
    /**
     * 算法
     */
    private String algorithm;
    /**
     * 盐
     */
    @Setter
    protected byte[] salt;
    /**
     * 加盐位置，即将盐值字符串放置在数据的index数，默认0
     */
    @Setter
    protected int saltPosition;
    /**
     * 散列次数
     */
    @Setter
    protected int digestCount;

    // ---------------------------------------------------------------- constructor

    /**
     * 构造
     *
     * @param algorithm 算法枚举
     */
    public DigestCrypto(DigestAlgorithm algorithm) {
        this(algorithm.getValue());
    }

    /**
     * 构造
     *
     * @param algorithm 算法枚举
     */
    public DigestCrypto(String algorithm) {
        this(algorithm, null);
    }

    public DigestCrypto(String algorithm, boolean useDefaultProvider) {
        if (useDefaultProvider) {
            try {
                this.digest = MessageDigest.getInstance(algorithm);
            } catch (NoSuchAlgorithmException e) {
                CryptoExceptionCheck.INSTANCE.throwUnchecked(e);
            }
        } else {
            this.init(algorithm, null);
        }
    }

    /**
     * 构造
     *
     * @param algorithm 算法
     * @param provider  算法提供者，null表示JDK默认，可以引入Bouncy Castle等来提供更多算法支持
     */
    public DigestCrypto(DigestAlgorithm algorithm, Provider provider) {
        init(algorithm.getValue(), provider);
    }

    /**
     * 构造
     *
     * @param algorithm 算法
     * @param provider  算法提供者，null表示JDK默认，可以引入Bouncy Castle等来提供更多算法支持
     */
    public DigestCrypto(String algorithm, Provider provider) {
        init(algorithm, provider);
    }

    /**
     * 构造
     *
     * @param digest {@link MessageDigest}
     */
    public DigestCrypto(MessageDigest digest) {
        this.digest = digest;
    }

    // ---------------------------------------------------------------- init

    /**
     * 初始化
     *
     * @param algorithm 算法
     * @param provider  算法提供者，null表示JDK默认，可以引入Bouncy Castle等来提供更多算法支持
     * @throws io.github.fishlikewater.raiden.core.exception.RaidenException Cause by IOException
     */
    public void init(String algorithm, Provider provider) {
        if (null == provider) {
            this.digest = RaidenCryptoUtils.createMessageDigest(algorithm);
        } else {
            try {
                this.digest = MessageDigest.getInstance(algorithm, provider);
            } catch (NoSuchAlgorithmException e) {
                CryptoExceptionCheck.INSTANCE.throwUnchecked(e);
            }
        }
    }

    // ---------------------------------------------------------------- common digest

    public void reset() {
        this.digest.reset();
    }

    /**
     * 生成文件摘要
     *
     * @param data        被摘要数据
     * @param charsetName 编码
     * @return 摘要
     */
    public byte[] digest(String data, String charsetName) {
        return this.digest(data, Charset.forName(charsetName));
    }

    /**
     * 生成文件摘要
     *
     * @param data    被摘要数据
     * @param charset 编码
     * @return 摘要
     */
    public byte[] digest(String data, Charset charset) {
        return this.digest(StringUtils.bytes(data, charset));
    }

    /**
     * 生成文件摘要
     *
     * @param data 被摘要数据
     * @return 摘要
     */
    public byte[] digest(String data) {
        return this.digest(data, StandardCharsets.UTF_8);
    }

    /**
     * 生成文件摘要，并转为16进制字符串
     *
     * @param data        被摘要数据
     * @param charsetName 编码
     * @return 摘要
     */
    public String digestHex(String data, String charsetName) {
        return digestHex(data, Charset.forName(charsetName));
    }

    /**
     * 生成文件摘要，并转为16进制字符串
     *
     * @param data    被摘要数据
     * @param charset 编码
     * @return 摘要
     */
    public String digestHex(String data, Charset charset) {
        return Hex.encodeHexStr(this.digest(data, charset));
    }

    /**
     * 生成文件摘要
     *
     * @param data 被摘要数据
     * @return 摘要
     */
    public String digestHex(String data) {
        return this.digestHex(data, StandardCharsets.UTF_8);
    }

    /**
     * 生成文件摘要<br>
     *
     * @param file 被摘要文件
     * @return 摘要bytes
     */
    public byte[] digest(File file) {
        try (InputStream in = FileUtils.getBufferInputStream(file)) {
            return this.digest(in);
        } catch (IOException e) {
            throw new RaidenCryptoException(e);
        }
    }

    /**
     * 生成文件摘要，并转为16进制字符串<br>
     *
     * @param file 被摘要文件
     * @return 摘要
     */
    public String digestHex(File file) {
        return Hex.encodeHexStr(this.digest(file));
    }

    /**
     * 生成摘要，考虑加盐和重复摘要次数
     *
     * @param data 数据bytes
     * @return 摘要bytes
     */
    public byte[] digest(byte[] data) {
        byte[] result;
        if (this.saltPosition <= 0) {
            // 加盐在开头，自动忽略空盐值
            result = this.doDigest(this.salt, data);
        } else if (this.saltPosition >= data.length) {
            // 加盐在末尾，自动忽略空盐值
            result = this.doDigest(data, this.salt);
        } else if (ObjectUtils.isNotNullOrEmpty(this.salt)) {
            // 加盐在中间
            this.digest.update(data, 0, this.saltPosition);
            this.digest.update(this.salt);
            this.digest.update(data, this.saltPosition, data.length - this.saltPosition);
            result = this.digest.digest();
        } else {
            // 无加盐
            result = this.doDigest(data);
        }

        return this.resetAndRepeatDigest(result);
    }

    /**
     * 生成摘要，并转为16进制字符串<br>
     *
     * @param data 被摘要数据
     * @return 摘要
     */
    public String digestHex(byte[] data) {
        return Hex.encodeHexStr(digest(data));
    }

    /**
     * 生成摘要
     *
     * @param data {@link InputStream} 数据流
     * @return 摘要bytes
     */
    public byte[] digest(InputStream data) {
        return digest(data, CommonConstants.DEFAULT_BUFFER_SIZE);
    }

    /**
     * 生成摘要，并转为16进制字符串<br>
     *
     * @param data 被摘要数据
     * @return 摘要
     */
    public String digestHex(InputStream data) {
        return Hex.encodeHexStr(digest(data));
    }

    /**
     * 生成摘要
     *
     * @param data {@link InputStream} 数据流
     * @return 摘要bytes
     */
    public byte[] digest(InputStream data, int bufferLength) {
        if (bufferLength < 1) {
            bufferLength = CommonConstants.DEFAULT_BUFFER_SIZE;
        }

        byte[] result;
        try {
            if (ObjectUtils.isNotNullOrEmpty(this.salt)) {
                result = digestWithoutSalt(data, bufferLength);
            } else {
                result = digestWithSalt(data, bufferLength);
            }
        } catch (IOException e) {
            throw new RaidenCryptoException(e);
        }

        return resetAndRepeatDigest(result);
    }

    /**
     * 生成摘要，并转为16进制字符串<br>
     *
     * @param data         被摘要数据
     * @param bufferLength 缓存长度
     * @return 摘要
     */
    public String digestHex(InputStream data, int bufferLength) {
        return Hex.encodeHexStr(digest(data, bufferLength));
    }

    /**
     * 获取散列长度，0表示不支持此方法
     *
     * @return 散列长度，0表示不支持此方法
     */
    public int getDigestLength() {
        return this.digest.getDigestLength();
    }

    // -------------------------------------------------------------------------------- Private method start

    /**
     * 生成摘要
     *
     * @param data         {@link InputStream} 数据流
     * @param bufferLength 缓存长度
     * @return 摘要bytes
     * @throws IOException 从流中读取数据引发的IO异常
     */
    private byte[] digestWithoutSalt(InputStream data, int bufferLength) throws IOException {
        final byte[] buffer = new byte[bufferLength];
        int read;
        while ((read = data.read(buffer, 0, bufferLength)) > -1) {
            this.digest.update(buffer, 0, read);
        }
        return this.digest.digest();
    }

    /**
     * 生成摘要
     *
     * @param data         {@link InputStream} 数据流
     * @param bufferLength 缓存长度
     * @return 摘要bytes
     * @throws IOException 从流中读取数据引发的IO异常
     */
    private byte[] digestWithSalt(InputStream data, int bufferLength) throws IOException {
        if (this.saltPosition <= 0) {
            // 加盐在开头
            this.digest.update(this.salt);
        }

        final byte[] buffer = new byte[bufferLength];
        int total = 0;
        int read;
        while ((read = data.read(buffer, 0, bufferLength)) > -1) {
            total += read;
            if (this.saltPosition > 0 && total >= this.saltPosition) {
                if (total != this.saltPosition) {
                    digest.update(buffer, 0, total - this.saltPosition);
                }
                // 加盐在中间
                this.digest.update(this.salt);
                this.digest.update(buffer, total - this.saltPosition, read);
            } else {
                this.digest.update(buffer, 0, read);
            }
        }

        if (total < this.saltPosition) {
            // 加盐在末尾
            this.digest.update(this.salt);
        }

        return this.digest.digest();
    }

    /**
     * 生成摘要
     *
     * @param bytes 数据bytes
     * @return 摘要bytes
     */
    private byte[] doDigest(byte[]... bytes) {
        for (byte[] data : bytes) {
            if (ObjectUtils.isNotNullOrEmpty(data)) {
                this.digest.update(data);
            }
        }
        return this.digest.digest();
    }

    /**
     * 重复计算摘要，取决于{@link #digestCount} 值<br>
     * 每次计算摘要前都会重置{@link #digest}
     *
     * @param digestData 第一次摘要过的数据
     * @return 摘要
     */
    private byte[] resetAndRepeatDigest(byte[] digestData) {
        final int digestCount = Math.max(1, this.digestCount);
        reset();
        for (int i = 0; i < digestCount - 1; i++) {
            digestData = doDigest(digestData);
            reset();
        }
        return digestData;
    }
}
