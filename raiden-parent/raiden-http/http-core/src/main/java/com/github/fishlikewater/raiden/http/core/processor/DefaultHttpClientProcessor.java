/*
 * Copyright © 2024 zhangxiang (fishlikewater@126.com)
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
package com.github.fishlikewater.raiden.http.core.processor;

import cn.hutool.core.util.TypeUtil;
import com.github.fishlikewater.raiden.http.core.HttpBootStrap;
import com.github.fishlikewater.raiden.http.core.HttpRequestClient;
import com.github.fishlikewater.raiden.http.core.MultipartData;
import com.github.fishlikewater.raiden.http.core.enums.HttpMethod;
import com.github.fishlikewater.raiden.http.core.interceptor.HttpClientInterceptor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.http.HttpClient;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * @author fishlikewater@126.com
 * @since 2021年12月26日 18:59
 * @version 1.0.0
 **/
@Slf4j
public class DefaultHttpClientProcessor implements HttpClientProcessor {

    @SneakyThrows(Throwable.class)
    @Override
    public Object handler(HttpMethod method,
                          Map<String, String> headMap,
                          Class<?> returnType,
                          Type typeArgument,
                          boolean form,
                          String url,
                          Map<String, String> paramMap,
                          Object bodyObject,
                          HttpClientInterceptor interceptor,
                          MultipartData multipartData,
                          HttpClient httpClient) {
        return request(url, method, form, returnType, TypeUtil.getClass(typeArgument),
                headMap, paramMap, bodyObject, interceptor, multipartData, httpClient);
    }

    private <T> Object request(String url,
                               HttpMethod method,
                               boolean form,
                               Class<?> returnType,
                               Class<T> typeArgument,
                               Map<String, String> headMap,
                               Map<String, String> paramMap,
                               Object bodyObject,
                               HttpClientInterceptor interceptor,
                               MultipartData multipartData,
                               HttpClient httpClient) throws IOException, InterruptedException {
        HttpRequestClient httpRequestClient = HttpBootStrap.getHttpRequestClient();
        if (returnType.isAssignableFrom(CompletableFuture.class)) {
            //异步
            return async(url, method, form, typeArgument, headMap, paramMap, bodyObject, interceptor, multipartData, httpClient, httpRequestClient);
        } else {
            //同步
            return sync(url, method, form, returnType, headMap, paramMap, bodyObject, interceptor, multipartData, httpClient, httpRequestClient);
        }
    }

    private static Object sync(String url,
                               HttpMethod method,
                               boolean form,
                               Class<?> returnType,
                               Map<String, String> headMap,
                               Map<String, String> paramMap,
                               Object bodyObject,
                               HttpClientInterceptor interceptor,
                               MultipartData multipartData,
                               HttpClient httpClient,
                               HttpRequestClient httpRequestClient) throws IOException, InterruptedException {
        if (form) {
            return httpRequestClient.formSync(method, url, headMap, paramMap, bodyObject, returnType, interceptor, httpClient);
        }
        if (Objects.nonNull(multipartData) && !multipartData.isFileDownload()) {
            return httpRequestClient.fileSync(method, url, headMap, bodyObject, returnType, interceptor, multipartData, httpClient);
        }
        switch (method) {
            case GET -> {
                return httpRequestClient.getSync(url, headMap, paramMap, returnType, interceptor, httpClient, multipartData);
            }
            case DELETE -> {
                return httpRequestClient.deleteSync(url, headMap, paramMap, returnType, interceptor, httpClient);
            }
            case POST -> {
                return httpRequestClient.postSync(url, headMap, bodyObject, returnType, interceptor, httpClient);
            }
            case PUT -> {
                return httpRequestClient.putSync(url, headMap, bodyObject, returnType, interceptor, httpClient);
            }
            case PATCH -> {
                return httpRequestClient.patchSync(url, headMap, bodyObject, returnType, interceptor, httpClient);
            }
            default -> {
                return "";
            }
        }
    }

    private static <T> Object async(String url,
                                    HttpMethod method,
                                    boolean form,
                                    Class<T> typeArgument,
                                    Map<String, String> headMap,
                                    Map<String, String> paramMap,
                                    Object bodyObject,
                                    HttpClientInterceptor interceptor,
                                    MultipartData multipartData,
                                    HttpClient httpClient,
                                    HttpRequestClient httpRequestClient) {
        if (form) {
            return httpRequestClient.formAsync(method, url, headMap, paramMap, bodyObject, typeArgument, interceptor, httpClient);
        }
        if (Objects.nonNull(multipartData) && !multipartData.isFileDownload()) {
            return httpRequestClient.fileAsync(method, url, headMap, bodyObject, typeArgument, interceptor, multipartData, httpClient);
        }
        switch (method) {
            case GET -> {
                return httpRequestClient.getAsync(url, headMap, paramMap, typeArgument, interceptor, httpClient, multipartData);
            }
            case DELETE -> {
                return httpRequestClient.deleteAsync(url, headMap, paramMap, typeArgument, interceptor, httpClient);
            }
            case POST -> {
                return httpRequestClient.postAsync(url, headMap, bodyObject, typeArgument, interceptor, httpClient);
            }
            case PUT -> {
                return httpRequestClient.putAsync(url, headMap, bodyObject, typeArgument, interceptor, httpClient);
            }
            case PATCH -> {
                return httpRequestClient.patchAsync(url, headMap, bodyObject, typeArgument, interceptor, httpClient);
            }
            default -> {
                return "";
            }
        }
    }
}
