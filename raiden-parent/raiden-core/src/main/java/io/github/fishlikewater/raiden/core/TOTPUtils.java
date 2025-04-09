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
package io.github.fishlikewater.raiden.core;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * {@code TOTPGenerator}
 * TOTP生成与验证
 *
 * @author zhangxiang
 * @since 1.1.5
 */
public class TOTPUtils {

    // 30 seconds
    private static final int TIME_STEP = 30000;

    private static final String HMAC_SHA1 = "HmacSHA1";

    /**
     * 生成TOTP
     *
     * @param secretKey 密钥
     * @return TOTP
     */
    public static String generateTOTP(String secretKey) {
        long time = System.currentTimeMillis() / TIME_STEP;
        String timeHex = Long.toHexString(time);
        byte[] timeBytes = Hex.decodeHex(timeHex.toCharArray());

        byte[] secretBytes = Base64.getDecoder().decode(secretKey);
        byte[] hmac = calculateHMAC(HMAC_SHA1, secretBytes, timeBytes);

        int offset = hmac[hmac.length - 1] & 0x0F;
        int code = ((hmac[offset] & 0x7f) << 24) |
                ((hmac[offset + 1] & 0xff) << 16) |
                ((hmac[offset + 2] & 0xff) << 8) |
                (hmac[offset + 3] & 0xff);

        return String.format("%06d", code % 1000000);
    }

    /**
     * 验证TOTP
     *
     * @param secretKey 密钥
     * @param inputCode 输入的TOTP
     * @return 是否验证成功
     */
    public static boolean verifyTOTP(String secretKey, String inputCode) {
        String generatedCode = generateTOTP(secretKey);
        return generatedCode.equals(inputCode);
    }

    /**
     * 计算HMAC
     *
     * @param algorithm 算法
     * @param key       密钥
     * @param data      数据
     * @return HMAC
     */
    @SuppressWarnings("SameParameterValue")
    private static byte[] calculateHMAC(String algorithm, byte[] key, byte[] data) {
        try {
            Mac mac = Mac.getInstance(algorithm);
            mac.init(new SecretKeySpec(key, algorithm));
            return mac.doFinal(data);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Error calculating HMAC", e);
        }
    }
}
