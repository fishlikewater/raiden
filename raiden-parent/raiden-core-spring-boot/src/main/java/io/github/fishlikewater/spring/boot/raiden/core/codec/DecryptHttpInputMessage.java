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
package io.github.fishlikewater.spring.boot.raiden.core.codec;

import lombok.NonNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author zhangxiang
 * @version 1.1.1
 * @since 2024/12/25
 **/
public class DecryptHttpInputMessage implements HttpInputMessage {
    private final HttpInputMessage inputMessage;
    private final CryptoHandler crypto;

    public DecryptHttpInputMessage(HttpInputMessage inputMessage, CryptoHandler crypto) {
        this.inputMessage = inputMessage;
        this.crypto = crypto;
    }

    @Override
    @NonNull
    public InputStream getBody() throws IOException {
        byte[] decrypt = crypto.decrypt(inputMessage.getBody().readAllBytes(), getHeaders());
        return new ByteArrayInputStream(decrypt);
    }

    @Override
    @NonNull
    public HttpHeaders getHeaders() {
        return inputMessage.getHeaders();
    }
}
