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
        String publicKeyBase64 = "MFkwEwYHKoZIzj0CAQYIKoEcz1UBgi0DQgAEQb1chkBOEGMGCmsQC0DRmpldWlkCcrMRTp/qcarpDVNb1zIbLXN1hsfq11B9UyBYR3RCw6CvLsRa7+PF3zjSVQ==";
        // 解码Base64字符串为字节数组
        byte[] encoded = Base64.getDecoder().decode(publicKeyBase64);

        byte[] uncompressed = RaidenCryptoUtils.sm2X509ConvertToUncompressedFormat(encoded);
        // 转换为 Base64
        String uncompressedKeyBase64 = Base64.getEncoder().encodeToString(uncompressed);
        System.out.println("非压缩公钥(Base64): " + uncompressedKeyBase64);
        System.out.println("非压缩公钥(HEX): " + Hex.encodeHexStr(uncompressed));
    }


    @Test
    public void test2() {
        String privateKey = "MIGTAgEAMBMGByqGSM49AgEGCCqBHM9VAYItBHkwdwIBAQQg8wg10NA9rhK/wyxD0hBihu9whVUuLn9YDf6RKryAhuigCgYIKoEcz1UBgi2hRANCAAR/4Qh4A7LW3SeGAXtvaTDRctFCk+6HFALDIUnhtHPng3vByPQ37Ux1i6aGAFObO2n16oPvvcE0XySMuV3PxZD1";
        String publicKey = "MFkwEwYHKoZIzj0CAQYIKoEcz1UBgi0DQgAEf+EIeAOy1t0nhgF7b2kw0XLRQpPuhxQCwyFJ4bRz54N7wcj0N+1MdYumhgBTmztp9eqD773BNF8kjLldz8WQ9Q==";
        SM2 sm2 = SmUtil.sm2(privateKey, publicKey);
        sm2.setMode(SM2Engine.Mode.C1C3C2);
        sm2.usePlainEncoding();
        String encodeStr = "BHJKjL2FnH+vIRIKT+uUSn+EKRgliromEilsGFtRffb8G1Zmbv6Io+0iPzYFOy7u2u81r6/5FbmTvltp2qjCyc9cjtoNpdKH4VYOZfqPd/yttPOSnyF1U9ixE6jRsKxsWnCy1DFii0wzgWgAGOensno=";
        String decryptStr = sm2.decryptStr(encodeStr, KeyType.PrivateKey);
        System.out.println(decryptStr);
    }
}
