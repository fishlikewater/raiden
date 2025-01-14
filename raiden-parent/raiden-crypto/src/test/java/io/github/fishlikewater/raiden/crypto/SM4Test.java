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

import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import io.github.fishlikewater.raiden.core.Hex;
import io.github.fishlikewater.raiden.crypto.symmetric.SM4;
import io.github.fishlikewater.raiden.crypto.symmetric.SymmetricAlgorithm;
import io.github.fishlikewater.raiden.crypto.symmetric.SymmetricUtils;
import org.junit.Test;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

/**
 * SM4Test
 *
 * @author zhangxiang
 * @version 1.1.1
 * @since 2024/12/25
 **/
public class SM4Test {

    @Test
    public void test() {
        String str = "hello, world";
        SecretKey secretKey = SymmetricUtils.generateKey(SymmetricAlgorithm.SM4.name(), -1);
        SM4 sm4 = new SM4(secretKey);
        byte[] decrypt = sm4.encrypt(str.getBytes());
        String hexStr = Hex.encodeHexStr(decrypt);
        System.out.println(hexStr);
        System.out.println(new String(sm4.decrypt(decrypt)));
    }

    @Test
    public void test2() {
        byte[] key = "1234567890123456".getBytes(StandardCharsets.UTF_8);
        cn.hutool.crypto.symmetric.SM4 sm4 = new cn.hutool.crypto.symmetric.SM4(Mode.ECB, Padding.PKCS5Padding, key);
        String s = sm4.decryptStr("994d4b9e2d8ea5ceb27657e19cb39489f6a7628e1a894c4c7c7433741a97f4b4f3294494f41fa3e6f87b72a26bfc97be");
        System.out.println(s);
    }
}
