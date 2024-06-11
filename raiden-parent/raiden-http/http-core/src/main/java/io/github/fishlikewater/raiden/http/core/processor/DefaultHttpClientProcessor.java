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

import io.github.fishlikewater.raiden.http.core.HttpBootStrap;
import io.github.fishlikewater.raiden.http.core.HttpRequestClient;
import io.github.fishlikewater.raiden.http.core.MultipartData;
import io.github.fishlikewater.raiden.http.core.RequestWrap;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Objects;
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

    private Object request(RequestWrap requestWrap) throws IOException, InterruptedException {
        if (requestWrap.getReturnType().isAssignableFrom(CompletableFuture.class)) {
            //异步
            return async(requestWrap);
        } else {
            //同步
            return sync(requestWrap);
        }
    }

    private static Object sync(RequestWrap requestWrap) throws IOException, InterruptedException {
        HttpRequestClient httpRequestClient = HttpBootStrap.getHttpRequestClient();
        if (requestWrap.isForm()) {
            return httpRequestClient.formSync(requestWrap);
        }
        MultipartData multipartData = requestWrap.getMultipartData();
        if (Objects.nonNull(multipartData) && !multipartData.isFileDownload()) {
            return httpRequestClient.fileSync(requestWrap);
        }
        switch (requestWrap.getHttpMethod()) {
            case GET -> {
                return httpRequestClient.getSync(requestWrap);
            }
            case DELETE -> {
                return httpRequestClient.deleteSync(requestWrap);
            }
            case POST -> {
                return httpRequestClient.postSync(requestWrap);
            }
            case PUT -> {
                return httpRequestClient.putSync(requestWrap);
            }
            case PATCH -> {
                return httpRequestClient.patchSync(requestWrap);
            }
            default -> {
                return "";
            }
        }
    }

    private static <T> Object async(RequestWrap requestWrap) {
        HttpRequestClient httpRequestClient = HttpBootStrap.getHttpRequestClient();
        if (requestWrap.isForm()) {
            return httpRequestClient.formAsync(requestWrap);
        }
        MultipartData multipartData = requestWrap.getMultipartData();
        if (Objects.nonNull(multipartData) && !multipartData.isFileDownload()) {
            return httpRequestClient.fileAsync(requestWrap);
        }
        switch (requestWrap.getHttpMethod()) {
            case GET -> {
                return httpRequestClient.getAsync(requestWrap);
            }
            case DELETE -> {
                return httpRequestClient.deleteAsync(requestWrap);
            }
            case POST -> {
                return httpRequestClient.postAsync(requestWrap);
            }
            case PUT -> {
                return httpRequestClient.putAsync(requestWrap);
            }
            case PATCH -> {
                return httpRequestClient.patchAsync(requestWrap);
            }
            default -> {
                return "";
            }
        }
    }
}
