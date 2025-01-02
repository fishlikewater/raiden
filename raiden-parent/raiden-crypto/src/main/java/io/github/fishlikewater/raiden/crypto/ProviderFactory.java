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

import java.security.Provider;
import java.security.Security;

/**
 * <p>
 * {@code ProviderFactory}
 * </p>
 * Provider工厂类
 *
 * @author fishlikewater@126.com
 * @version 1.0.2
 * @since 2024年06月10日 9:23
 **/
public class ProviderFactory {

    /**
     * 创建Bouncy Castle 提供者
     *
     * @return {@link Provider}
     */
    public static Provider createBouncyCastleProvider() throws Exception {
        Class<?> aClass = Class.forName("org.bouncycastle.jce.provider.BouncyCastleProvider");
        Provider provider = (Provider) aClass.getConstructor().newInstance();
        Security.insertProviderAt(provider, 0);
        return provider;
    }
}
