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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.fishlikewater.raiden.core.exception.RaidenExceptionCheck;
import io.github.fishlikewater.raiden.core.model.Result;
import io.github.fishlikewater.spring.boot.raiden.core.SpringUtils;
import io.github.fishlikewater.spring.boot.raiden.core.annotation.Crypto;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Objects;

/**
 * @author zhangxiang
 * @version 1.1.1
 * @since 2024/12/25
 **/
@RequiredArgsConstructor
@ControllerAdvice
public class EncryptResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    private final ObjectMapper objectMapper;

    @Override
    public boolean supports(@Nonnull MethodParameter returnType, @Nonnull Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object beforeBodyWrite(Object body, @Nonnull MethodParameter parameter, @Nonnull MediaType selectedContentType,
                                  @Nonnull Class<? extends HttpMessageConverter<?>> selectedConverterType, @Nonnull ServerHttpRequest request, @Nonnull ServerHttpResponse response) {
        Crypto cryptoAnnotation = NeedCrypto.getCryptoAnnotation(parameter);
        if (Objects.isNull(cryptoAnnotation)) {
            return body;
        }

        if (!(body instanceof Result)) {
            return body;
        }
        CryptoHandler cryptoHandler = SpringUtils.getBean(cryptoAnnotation.crypto());

        Result<Object> result = (Result<Object>) body;
        Object data = result.getResult();
        if (null == data) {
            return body;
        }

        String xx = null;
        Class<?> dataClass = data.getClass();
        if (dataClass.isPrimitive() || (data instanceof String)) {
            xx = String.valueOf(data);
        } else {
            //JavaBean、Map、List等先序列化
            try {
                xx = objectMapper.writeValueAsString(data);
            } catch (JsonProcessingException e) {
                RaidenExceptionCheck.INSTANCE.throwUnchecked(e);
            }
        }
        if (Objects.isNull(xx)) {
            return result;
        }

        String encrypt = cryptoHandler.encrypt(xx.getBytes(), response);
        result.setResult(encrypt);

        return result;
    }


}
