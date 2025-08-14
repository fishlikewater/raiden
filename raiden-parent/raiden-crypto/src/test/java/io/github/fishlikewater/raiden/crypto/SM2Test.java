/*
 * Copyright (c) 2023-2025 zhangxiang (fishlikewater@126.com)
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

import cn.hutool.core.util.HexUtil;
import cn.hutool.crypto.KeyUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.SM2;
import io.github.fishlikewater.raiden.core.Hex;
import org.bouncycastle.crypto.engines.SM2Engine;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.Test;

import java.security.KeyPair;
import java.security.Provider;
import java.security.Security;
import java.util.Base64;

/**
 * {@code SM2Test}
 * sm2测试
 *
 * @author zhangxiang
 * @version 1.1.2
 * @since 2025/01/13
 */
public class SM2Test {

    @Test
    public void test() {
        KeyPair sm2 = KeyUtil.generateKeyPair("SM2");
        System.out.println(Base64.getEncoder().encodeToString(sm2.getPublic().getEncoded()));
        System.out.println(Base64.getEncoder().encodeToString(sm2.getPrivate().getEncoded()));
    }

    @Test
    public void test1() {
        if (Security.getProvider("BC") == null) {
            Security.addProvider(new BouncyCastleProvider());
        }
        for (Provider provider : Security.getProviders()) {
            System.out.println(provider.getName() + " - " + provider.getInfo());
        }
        String publicKeyBase64 = "MFkwEwYHKoZIzj0CAQYIKoEcz1UBgi0DQgAE4nQd+I2k1Yq3kUd4nLzGZgTVuzThhwtFmICBP8aDznnSqfa3rai9OvelMpoohDLsUZ3xcdgovgPhKGNNY7GRQA==";
        // 解码Base64字符串为字节数组
        byte[] encoded = Base64.getDecoder().decode(publicKeyBase64);

        byte[] uncompressed = RaidenCryptoUtils.sm2X509ConvertToUncompressedFormat(encoded);
        byte[] x509Format = RaidenCryptoUtils.sm2UncompressedConvertToX509Format(uncompressed);
        // 转换为 Base64
        String uncompressedKeyBase64 = Base64.getEncoder().encodeToString(uncompressed);
        String x509KeyBase64 = Base64.getEncoder().encodeToString(x509Format);
        System.out.println("非压缩公钥(Base64): " + uncompressedKeyBase64);
        System.out.println("非压缩公钥(HEX): " + Hex.encodeHexStr(uncompressed));
        System.out.println("X509格式公钥(Base64): " + x509KeyBase64);
        System.out.println(x509KeyBase64.equals(publicKeyBase64));
    }


    @Test
    public void test2() {
        String privateKey = "MIGTAgEAMBMGByqGSM49AgEGCCqBHM9VAYItBHkwdwIBAQQgRURloM9UguhuMo3jskAsiIi5OeSxOmK/apuU4q7gylygCgYIKoEcz1UBgi2hRANCAATidB34jaTVireRR3icvMZmBNW7NOGHC0WYgIE/xoPOedKp9retqL0696UymiiEMuxRnfFx2Ci+A+EoY01jsZFA";
        String publicKey = "MFkwEwYHKoZIzj0CAQYIKoEcz1UBgi0DQgAE4nQd+I2k1Yq3kUd4nLzGZgTVuzThhwtFmICBP8aDznnSqfa3rai9OvelMpoohDLsUZ3xcdgovgPhKGNNY7GRQA==";
        SM2 sm2 = SmUtil.sm2(privateKey, publicKey);
        sm2.setMode(SM2Engine.Mode.C1C3C2);
        sm2.usePlainEncoding();
        String s = sm2.encryptBase64("123456789012347", KeyType.PublicKey);
        System.out.println(HexUtil.encodeHexStr(Base64.getDecoder().decode(s)));
        String s1 = sm2.decryptStr(s, KeyType.PrivateKey);
        System.out.println(s1);

        String encodeStr = "BEBaXHrm6RM0XbdMq9wqA8xhribrE59/NhMqO/SMyt7sH73hINotC/RmwpDbTuKioEguFunNoEsUnklQkWjQU51iW4zhb/Kk71j9m6+0BERwND4GRqPbYcPmNsIGgVN7bDwXMj4LU9BIvDpe";
        String decryptStr = sm2.decryptStr(encodeStr, KeyType.PrivateKey);
        System.out.println(decryptStr);
    }
}
