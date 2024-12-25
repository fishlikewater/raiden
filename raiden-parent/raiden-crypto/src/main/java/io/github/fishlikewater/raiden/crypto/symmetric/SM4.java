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

import javax.crypto.SecretKey;
import java.security.spec.AlgorithmParameterSpec;

/**
 * SM4
 *
 * @author zhangxiang
 * @version 1.1.1
 * @since 2024/12/25
 **/
public class SM4 extends SymmetricCrypto {

    public SM4(SecretKey secretKey) {
        super(SymmetricAlgorithm.SM4.getValue(), secretKey, null);
    }

    public SM4(SecretKey key, AlgorithmParameterSpec paramsSpec) {
        super(SymmetricAlgorithm.SM4.getValue(), key, paramsSpec);
    }
}
