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
 * {@code RaidenCryptoUtils}
 *
 * @author zhangxiang
 * @version 1.0.0
 * @since 2024/05/30
 */
public final class RaidenCryptoUtils {

    private static Provider provider;

    static {
        try {
            provider = ProviderFactory.createBouncyCastleProvider();
        } catch (Exception ignore) {
        }
    }

    public static MessageDigest createMessageDigest(String algorithm) {
        MessageDigest messageDigest;
        try {
            messageDigest = (null == provider) ? MessageDigest.getInstance(algorithm) : MessageDigest.getInstance(algorithm, provider);
        } catch (NoSuchAlgorithmException e) {
            return CryptoExceptionCheck.INSTANCE.throwUnchecked(e);
        }

        return messageDigest;
    }
}
