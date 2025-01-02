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

import io.github.fishlikewater.spring.boot.raiden.core.SpringUtils;
import io.github.fishlikewater.spring.boot.raiden.core.annotation.Crypto;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import java.lang.reflect.Type;
import java.util.Objects;

/**
 * @author zhangxiang
 * @version 1.1.1
 * @since 2024/12/25
 **/
@RequiredArgsConstructor
@ControllerAdvice
public class DecryptRequestBodyAdvice implements RequestBodyAdvice {

    @Override
    public boolean supports(@Nonnull MethodParameter methodParameter, @Nonnull Type targetType,
                            @Nonnull Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object handleEmptyBody(Object body, @Nonnull HttpInputMessage inputMessage, @Nonnull MethodParameter parameter,
                                  @Nonnull Type targetType, @Nonnull Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }

    @Nonnull
    @Override
    public HttpInputMessage beforeBodyRead(@Nonnull HttpInputMessage inputMessage, @Nonnull MethodParameter parameter, @Nonnull Type targetType,
                                           @Nonnull Class<? extends HttpMessageConverter<?>> converterType) {
        Crypto cryptoAnnotation = NeedCrypto.getCryptoAnnotation(parameter);
        if (Objects.nonNull(cryptoAnnotation)) {
            CryptoHandler cryptoHandler = SpringUtils.getBean(cryptoAnnotation.crypto());
            return new DecryptHttpInputMessage(inputMessage, cryptoHandler);
        }
        return inputMessage;
    }

    @Nonnull
    @Override
    public Object afterBodyRead(@Nonnull Object body, @Nonnull HttpInputMessage inputMessage, @Nonnull MethodParameter parameter, @Nonnull Type targetType,
                                @Nonnull Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }
}
