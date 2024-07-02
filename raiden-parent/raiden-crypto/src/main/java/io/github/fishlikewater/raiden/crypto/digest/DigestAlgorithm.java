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

import lombok.Getter;

/**
 * <p>
 * {@code DigestAlgorithm}
 * </p>
 * 摘要算法
 *
 * @author fishlikewater@126.com
 * @version 1.0.2
 * @since 2024年06月10日 9:33
 **/
@Getter
public enum DigestAlgorithm {

    // MD2 MD5 SHA1 SHA256 SHA384 SHA512
    MD2("MD2"),
    MD5("MD5"),
    SM3("SM3"),
    SHA1("SHA-1"),
    SHA256("SHA-256"),
    SHA384("SHA-384"),
    SHA512("SHA-512")

    ;


    private final String value;

    /**
     * 构造
     *
     * @param value 算法字符串表示
     */
    DigestAlgorithm(String value) {
        this.value = value;
    }
}
