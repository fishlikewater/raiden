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
package io.github.fishlikewater.raiden.crypto.symmetric;

import lombok.Getter;

/**
 * {@code SymmetricAlgorithm}
 * 对称加密算法
 *
 * @author zhangxiang
 * @version 1.0.3
 * @since 2024/07/02
 */
@SuppressWarnings("all")
public enum SymmetricAlgorithm {

    /**
     * 默认的AES加密方式：AES/ECB/PKCS5Padding
     */
    AES("AES"),
    /**
     * 默认的DES加密方式：DES/ECB/PKCS5Padding
     */
    DES("DES"),
    /**
     * 3DES算法，默认实现为：DESede/ECB/PKCS5Padding
     */
    DESede("DESede"),
    RC2("RC2"),

    /**
     * SM4算法
     */
    SM4("SM4"),

    PBEWithMD5AndDES("PBEWithMD5AndDES"),
    PBEWithSHA1AndDESede("PBEWithSHA1AndDESede"),
    PBEWithSHA1AndRC2_40("PBEWithSHA1AndRC2_40");;

    @Getter
    private final String value;

    /**
     * 构造
     *
     * @param value 算法字符串表示
     */
    SymmetricAlgorithm(String value) {
        this.value = value;
    }
}
