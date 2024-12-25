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
package io.github.fishlikewater.spring.boot.raiden.core.codec;


import org.springframework.http.HttpHeaders;
import org.springframework.http.server.ServerHttpResponse;

/**
 * @author zhangxiang
 * @version 1.1.1
 * @since 2024/12/25
 */
public interface CryptoHandler {
    /**
     * 加密
     *
     * @param body     待加密字节数组
     * @param response {@code ServerHttpResponse}
     * @return 加密后的字符串
     */
    String encrypt(byte[] body, ServerHttpResponse response);

    /**
     * 解密
     *
     * @param body        待解密字节数组
     * @param httpHeaders 请求头
     * @return 加密后的
     */
    byte[] decrypt(byte[] body, HttpHeaders httpHeaders);
}
