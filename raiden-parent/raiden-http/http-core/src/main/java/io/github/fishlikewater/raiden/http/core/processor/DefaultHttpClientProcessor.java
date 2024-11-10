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
package io.github.fishlikewater.raiden.http.core.processor;

import io.github.fishlikewater.raiden.core.ObjectUtils;
import io.github.fishlikewater.raiden.http.core.HttpBootStrap;
import io.github.fishlikewater.raiden.http.core.RequestWrap;
import io.github.fishlikewater.raiden.http.core.client.AbstractHttpRequestClient;
import io.github.fishlikewater.raiden.http.core.interceptor.HttpClientInterceptor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author fishlikewater@126.com
 * @version 1.0.0
 * @since 2021年12月26日 18:59
 **/
@Slf4j
public class DefaultHttpClientProcessor implements HttpClientProcessor {

    @SneakyThrows(Throwable.class)
    @Override
    public Object handler(RequestWrap requestWrap) {
        return request(requestWrap);
    }

    private Object request(RequestWrap requestWrap) {
        if (HttpBootStrap.getConfig().isEnableLog()) {
            List<HttpClientInterceptor> interceptors = requestWrap.getInterceptors();
            if (ObjectUtils.isNullOrEmpty(interceptors)) {
                requestWrap.addInterceptor(HttpBootStrap.getConfig().getLogInterceptor());
            } else {
                interceptors.addLast(HttpBootStrap.getConfig().getLogInterceptor());
            }
        }

        if (requestWrap.getReturnType().isAssignableFrom(CompletableFuture.class)) {
            //异步
            return async(requestWrap);
        } else {
            //同步
            return sync(requestWrap);
        }
    }

    private static Object sync(RequestWrap requestWrap) {
        AbstractHttpRequestClient httpRequestClient = HttpBootStrap.getConfig().getHttpClient();
        return httpRequestClient.requestSync(requestWrap);
    }

    private static Object async(RequestWrap requestWrap) {
        AbstractHttpRequestClient httpRequestClient = HttpBootStrap.getConfig().getHttpClient();
        return httpRequestClient.requestAsync(requestWrap);
    }
}
