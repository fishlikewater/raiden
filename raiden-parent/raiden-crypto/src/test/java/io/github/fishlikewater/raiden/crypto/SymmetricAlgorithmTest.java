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

import io.github.fishlikewater.raiden.core.Hex;
import io.github.fishlikewater.raiden.crypto.symmetric.SymmetricAlgorithm;
import io.github.fishlikewater.raiden.crypto.symmetric.SymmetricCrypto;
import io.github.fishlikewater.raiden.crypto.symmetric.SymmetricUtils;
import org.junit.Test;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

/**
 * {@code SymmetricAlgorithmTest}
 *
 * @author zhangxiang
 * @version 1.0.3
 * @since 2024/07/04
 */
public class SymmetricAlgorithmTest {

    @Test
    public void testSimpleAlgorithm() {
        SecretKey secretKey = SymmetricUtils.generateKey(SymmetricAlgorithm.AES.name(), -1);
        System.out.println(secretKey.getEncoded().length);
        String chars = Hex.encodeHexStr(secretKey.getEncoded(), false);
        System.out.println(chars);

        SymmetricCrypto symmetricCrypto = new SymmetricCrypto(SymmetricAlgorithm.AES.name(), secretKey, null);
        byte[] origin = "test hell world, this test crypto length change".getBytes(StandardCharsets.UTF_8);

        byte[] encrypt = symmetricCrypto.encrypt(origin);
        System.out.println(Hex.encodeHex(encrypt, false));

        byte[] decrypted = symmetricCrypto.decrypt(encrypt);
        System.out.println(new String(decrypted, StandardCharsets.UTF_8));
    }
}
