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
package io.github.fishlikewater.raiden.http.core;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.json.JSONUtil;
import io.github.fishlikewater.raiden.core.ObjectUtils;
import io.github.fishlikewater.raiden.core.StringUtils;
import io.github.fishlikewater.raiden.http.core.constant.HttpConstants;
import io.github.fishlikewater.raiden.http.core.enums.HttpMethod;
import io.github.fishlikewater.raiden.http.core.exception.RaidenHttpException;
import io.github.fishlikewater.raiden.http.core.interceptor.HttpClientInterceptor;
import io.github.fishlikewater.raiden.http.core.interceptor.LogInterceptor;
import io.github.fishlikewater.raiden.http.core.processor.MultiFileBodyProvider;
import io.github.fishlikewater.raiden.http.core.processor.ResponseJsonHandlerSubscriber;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * {@code HttpRequestClient}
 *
 * @author zhangxiang
 * @version 1.0.0
 * @since 2023/11/27
 */
@Slf4j
public class HttpRequestClient extends AbstractHttpRequestClient {

    private static final LogInterceptor LOG_INTERCEPTOR = new LogInterceptor();

    @Override
    public <T> CompletableFuture<T> requestAsync(RequestWrap requestWrap) {
        if (requestWrap.isForm()) {
            return this.formAsync(requestWrap);
        }
        MultipartData multipartData = requestWrap.getMultipartData();
        if (Objects.nonNull(multipartData) && !multipartData.isFileDownload()) {
            return this.fileAsync(requestWrap);
        }
        switch (requestWrap.getHttpMethod()) {
            case GET -> {
                return this.getAsync(requestWrap);
            }
            case DELETE -> {
                return this.deleteAsync(requestWrap);
            }
            case POST -> {
                return this.postAsync(requestWrap);
            }
            case PUT -> {
                return this.putAsync(requestWrap);
            }
            case PATCH -> {
                return this.patchAsync(requestWrap);
            }
            default -> {
                return null;
            }
        }
    }

    @Override
    public <T> T requestSync(RequestWrap requestWrap) throws IOException, InterruptedException {
        if (requestWrap.isForm()) {
            return this.formSync(requestWrap);
        }
        MultipartData multipartData = requestWrap.getMultipartData();
        if (Objects.nonNull(multipartData) && !multipartData.isFileDownload()) {
            return this.fileSync(requestWrap);
        }
        switch (requestWrap.getHttpMethod()) {
            case GET -> {
                return this.getSync(requestWrap);
            }
            case DELETE -> {
                return this.deleteSync(requestWrap);
            }
            case POST -> {
                return this.postSync(requestWrap);
            }
            case PUT -> {
                return this.putSync(requestWrap);
            }
            case PATCH -> {
                return this.patchSync(requestWrap);
            }
            default -> {
                return null;
            }
        }
    }

    @Override
    <T> CompletableFuture<T> getAsync(RequestWrap requestWrap) {
        this.checkHttpMethod(requestWrap, HttpMethod.GET);
        HttpRequest httpRequest = getHttpRequest(requestWrap);
        return handlerAsync(requestWrap, httpRequest);
    }

    @Override
    <T> T getSync(RequestWrap requestWrap) throws IOException, InterruptedException {
        this.checkHttpMethod(requestWrap, HttpMethod.GET);
        HttpRequest httpRequest = getHttpRequest(requestWrap);
        return handlerSync(requestWrap, httpRequest);
    }

    @Override
    <T> CompletableFuture<T> deleteAsync(RequestWrap requestWrap) {
        this.checkHttpMethod(requestWrap, HttpMethod.DELETE);
        HttpRequest httpRequest = getHttpRequest(requestWrap);
        return handlerAsync(requestWrap, httpRequest);
    }

    @Override
    <T> T deleteSync(RequestWrap requestWrap) throws IOException, InterruptedException {
        this.checkHttpMethod(requestWrap, HttpMethod.DELETE);
        HttpRequest httpRequest = getHttpRequest(requestWrap);
        return handlerSync(requestWrap, httpRequest);
    }

    @Override
    <T> CompletableFuture<T> postAsync(RequestWrap requestWrap) {
        this.checkHttpMethod(requestWrap, HttpMethod.POST);
        HttpRequest httpRequest = getHttpRequestBody(requestWrap);
        return handlerAsync(requestWrap, httpRequest);
    }

    @Override
    <T> T postSync(RequestWrap requestWrap) throws IOException, InterruptedException {
        this.checkHttpMethod(requestWrap, HttpMethod.POST);
        HttpRequest httpRequest = getHttpRequestBody(requestWrap);
        return handlerSync(requestWrap, httpRequest);
    }

    @Override
    <T> CompletableFuture<T> putAsync(RequestWrap requestWrap) {
        this.checkHttpMethod(requestWrap, HttpMethod.PUT);
        HttpRequest httpRequest = getHttpRequestBody(requestWrap);
        return handlerAsync(requestWrap, httpRequest);
    }

    @Override
    <T> T putSync(RequestWrap requestWrap) throws IOException, InterruptedException {
        this.checkHttpMethod(requestWrap, HttpMethod.PUT);
        HttpRequest httpRequest = getHttpRequestBody(requestWrap);
        return handlerSync(requestWrap, httpRequest);
    }

    @Override
    <T> CompletableFuture<T> patchAsync(RequestWrap requestWrap) {
        this.checkHttpMethod(requestWrap, HttpMethod.PATCH);
        HttpRequest httpRequest = getHttpRequestBody(requestWrap);
        return handlerAsync(requestWrap, httpRequest);
    }

    @Override
    <T> T patchSync(RequestWrap requestWrap) throws IOException, InterruptedException {
        this.checkHttpMethod(requestWrap, HttpMethod.PATCH);
        HttpRequest httpRequest = getHttpRequestBody(requestWrap);
        return handlerSync(requestWrap, httpRequest);
    }

    @Override
    <T> CompletableFuture<T> fileAsync(RequestWrap requestWrap) {
        HttpRequest httpRequest = getFileHttpRequest(requestWrap);
        return handlerAsync(requestWrap, httpRequest);
    }

    @Override
    <T> T fileSync(RequestWrap requestWrap) throws IOException, InterruptedException {
        HttpRequest httpRequest = getFileHttpRequest(requestWrap);
        return handlerSync(requestWrap, httpRequest);
    }

    @Override
    <T> CompletableFuture<T> formAsync(RequestWrap requestWrap) {
        HttpRequest httpRequest = getFormHttpRequest(requestWrap);
        return handlerAsync(requestWrap, httpRequest);
    }

    @Override
    <T> T formSync(RequestWrap requestWrap) throws IOException, InterruptedException {
        HttpRequest httpRequest = getFormHttpRequest(requestWrap);
        return handlerSync(requestWrap, httpRequest);
    }

    // ---------------------------------------------------------------- private

    private void checkHttpMethod(RequestWrap requestWrap, HttpMethod httpMethod) {
        if (ObjectUtils.notEquals(requestWrap.getHttpMethod(), httpMethod)) {
            throw new RaidenHttpException("The current calling method only supports {}", httpMethod.name());
        }
    }

    /**
     * 生成一个随机的boundary字符串
     *
     * @return {@link String}
     */
    private String boundaryString() {
        return StringUtils.format("Boundary {}", System.currentTimeMillis());
    }

    private HttpRequest getHttpRequest(RequestWrap requestWrap) {
        URI uri = URI.create(this.getRequestUrl(requestWrap.getUrl(), requestWrap.getParamMap()));
        final HttpRequest.Builder builder = HttpRequest.newBuilder().uri(uri);
        if (Objects.nonNull(requestWrap.getHeadMap())) {
            requestWrap.getHeadMap().forEach(builder::header);
        }
        if (requestWrap.getHttpMethod() == HttpMethod.DELETE) {
            builder.DELETE();
        } else {
            builder.GET();
        }
        HttpRequest httpRequest = builder.build();
        httpRequest = requestBefore(requestWrap.getInterceptor(), httpRequest);
        printLog(httpRequest);
        return httpRequest;
    }

    private HttpRequest getHttpRequestBody(RequestWrap requestWrap) {
        String body = Objects.isNull(requestWrap.getBodyObject()) ? "" : JSONUtil.toJsonStr(requestWrap.getBodyObject());
        HttpRequest.BodyPublisher requestBody = HttpRequest.BodyPublishers.ofString(body);
        final HttpRequest.Builder builder = HttpRequest.newBuilder()
                .method(requestWrap.getHttpMethod().name(), requestBody)
                .uri(URI.create(requestWrap.getUrl()));

        Map<String, String> headMap = requestWrap.getHeadMap();
        if (Objects.nonNull(headMap)) {
            headMap.forEach(builder::header);
        }
        if (Objects.isNull(headMap.get(HttpConstants.CONTENT_TYPE))) {
            builder.header(HttpConstants.CONTENT_TYPE, HttpConstants.CONTENT_TYPE_JSON);
        }
        HttpRequest httpRequest = builder.build();
        httpRequest = requestBefore(requestWrap.getInterceptor(), httpRequest);
        printLog(httpRequest);
        return httpRequest;
    }

    private HttpRequest getFormHttpRequest(RequestWrap requestWrap) {
        String url = requestWrap.getUrl();
        Map<String, String> headMap = requestWrap.getHeadMap();
        Map<String, String> paramMap = requestWrap.getParamMap();
        Object bodyObject = requestWrap.getBodyObject();
        if (Objects.isNull(requestWrap.getHttpClient())) {
            requestWrap.setHttpClient(HttpBootStrap.getHttpClient(HttpConstants.DEFAULT));
        }
        if (Objects.nonNull(paramMap) && !paramMap.isEmpty()) {
            url = this.getRequestUrl(url, paramMap);
        }

        HttpRequest.BodyPublisher requestBody;
        StringBuilder params = new StringBuilder("rd=").append(Math.random());
        if (Objects.nonNull(bodyObject)) {
            if (bodyObject instanceof Map<?, ?> map) {
                for (Map.Entry<?, ?> item : map.entrySet()) {
                    String param = StringUtils.format("&{}={}", item.getKey().toString().trim(), item.getValue().toString().trim());
                    params.append(param);
                }
            } else {
                final Map<String, Object> map = BeanUtil.beanToMap(bodyObject);
                for (Map.Entry<?, ?> item : map.entrySet()) {
                    String param = StringUtils.format("&{}={}", item.getKey().toString().trim(), item.getValue().toString().trim());
                    params.append(param);
                }
            }
        }
        requestBody = HttpRequest.BodyPublishers.ofString(params.toString());
        final HttpRequest.Builder builder = HttpRequest.newBuilder().method(requestWrap.getHttpMethod().name(), requestBody).uri(URI.create(url));
        if (Objects.nonNull(headMap)) {
            headMap.forEach(builder::header);
        }
        if (Objects.isNull(headMap.get(HttpConstants.CONTENT_TYPE))) {
            builder.header(HttpConstants.CONTENT_TYPE, HttpConstants.CONTENT_TYPE_FORM);
        }
        HttpRequest httpRequest = builder.build();
        httpRequest = requestBefore(requestWrap.getInterceptor(), httpRequest);
        printLog(httpRequest);
        return httpRequest;
    }

    private HttpRequest getFileHttpRequest(RequestWrap requestWrap) {
        if (Objects.isNull(requestWrap.getHttpClient())) {
            requestWrap.setHttpClient(HttpBootStrap.getHttpClient(HttpConstants.DEFAULT));
        }
        final String boundaryString = this.boundaryString();
        Map<String, String> headMap = requestWrap.getHeadMap();
        final HttpRequest.Builder builder = HttpRequest.newBuilder().uri(URI.create(requestWrap.getUrl()));
        if (Objects.nonNull(headMap)) {
            headMap.forEach(builder::header);
        }
        builder.header("Content-Type", StringUtils.format("multipart/form-data; boundary={}", boundaryString));
        HttpRequest.BodyPublisher requestBody = new MultiFileBodyProvider(requestWrap.getMultipartData(), requestWrap.getBodyObject(), boundaryString);
        builder.method(requestWrap.getHttpMethod().name(), requestBody);
        HttpRequest httpRequest = builder.build();
        httpRequest = requestBefore(requestWrap.getInterceptor(), httpRequest);
        printLog(httpRequest);
        return httpRequest;
    }

    @SuppressWarnings("unchecked")
    private <T> CompletableFuture<T> handlerAsync(RequestWrap requestWrap, HttpRequest httpRequest) {
        Class<?> typeArgumentClass = requestWrap.getTypeArgumentClass();
        HttpClientInterceptor interceptor = requestWrap.getInterceptor();
        MultipartData multipartData = requestWrap.getMultipartData();
        HttpClient httpClient = requestWrap.getHttpClient();
        if (ObjectUtils.isNullOrEmpty(httpClient)) {
            httpClient = HttpBootStrap.getHttpClient(HttpConstants.DEFAULT);
        }
        if (typeArgumentClass.isAssignableFrom(byte[].class)) {
            CompletableFuture<byte[]> completableFuture = httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofByteArray())
                    .thenApply(res -> requestAfter(res, interceptor).body());
            return (CompletableFuture<T>) completableFuture;
        }
        if (typeArgumentClass.isAssignableFrom(Path.class) && Objects.nonNull(multipartData)) {
            final Path path = multipartData.getPath();
            Assert.notNull(path, "Please pass in the file save path");
            if (path.toFile().isDirectory()) {
                httpClient
                        .sendAsync(httpRequest, HttpResponse.BodyHandlers.ofFileDownload(path, multipartData.getOpenOptions()))
                        .thenApply(res -> requestAfter(res, interceptor).body());
            } else {
                httpClient
                        .sendAsync(httpRequest, HttpResponse.BodyHandlers.ofFile(path, multipartData.getOpenOptions()))
                        .thenApply(res -> requestAfter(res, interceptor).body());
            }
        }
        return httpClient
                .sendAsync(httpRequest, (responseInfo) -> new ResponseJsonHandlerSubscriber<T>(responseInfo.headers(), typeArgumentClass))
                .thenApply(res -> requestAfter(res, interceptor).body());
    }

    @SuppressWarnings("unchecked")
    private <T> T handlerSync(RequestWrap requestWrap, HttpRequest httpRequest) throws IOException, InterruptedException {
        Class<?> returnType = requestWrap.getReturnType();
        HttpClient httpClient = requestWrap.getHttpClient();
        MultipartData multipartData = requestWrap.getMultipartData();
        HttpClientInterceptor interceptor = requestWrap.getInterceptor();
        if (ObjectUtils.isNullOrEmpty(httpClient)) {
            httpClient = HttpBootStrap.getHttpClient(HttpConstants.DEFAULT);
        }
        if (returnType.isAssignableFrom(byte[].class)) {
            final HttpResponse<byte[]> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofByteArray());
            return requestAfter((HttpResponse<T>) response, interceptor).body();
        }
        if (returnType.isAssignableFrom(Path.class) && Objects.nonNull(multipartData)) {
            final Path path = multipartData.getPath();
            Assert.notNull(path, "Please pass in the file save path");
            HttpResponse<Path> response;
            if (path.toFile().isDirectory()) {
                response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofFileDownload(path, multipartData.getOpenOptions()));
            } else {
                response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofFile(path, multipartData.getOpenOptions()));
            }
            return requestAfter((HttpResponse<T>) response, interceptor).body();
        }
        final HttpResponse<T> response = httpClient.send(httpRequest, (responseInfo) -> new ResponseJsonHandlerSubscriber<>(responseInfo.headers(), returnType));
        return requestAfter(response, interceptor).body();
    }

    private HttpRequest requestBefore(HttpClientInterceptor interceptor, HttpRequest httpRequest) {
        if (Objects.nonNull(interceptor)) {
            return interceptor.requestBefore(httpRequest);
        }
        return httpRequest;
    }

    private <T> HttpResponse<T> requestAfter(HttpResponse<T> response, HttpClientInterceptor interceptor) {
        if (HttpBootStrap.getLogConfig().isEnableLog()) {
            response = LOG_INTERCEPTOR.requestAfter(response);
        }
        if (Objects.nonNull(interceptor)) {
            response = interceptor.requestAfter(response);
        }
        return response;
    }

    private void printLog(HttpRequest httpRequest) {
        if (HttpBootStrap.getLogConfig().isEnableLog()) {
            LOG_INTERCEPTOR.requestBefore(httpRequest);
        }
    }

    private String getRequestUrl(String url, Map<String, String> map) {
        if (Objects.isNull(map) || map.isEmpty()) {
            return url;
        } else {
            StringBuilder newUrl = new StringBuilder(url);
            if (!url.contains(HttpConstants.URL_PARAMETER_SPLIT)) {
                newUrl.append("?rd=").append(Math.random());
            }
            for (Map.Entry<String, String> item : map.entrySet()) {
                try {
                    String param = StringUtils.format("&{}={}", item.getKey().trim(), URLEncoder.encode(item.getValue().trim(), StandardCharsets.UTF_8));
                    newUrl.append(param);
                } catch (Exception e) {
                    throw new RaidenHttpException(e, "join params error");
                }
            }
            return newUrl.toString();
        }
    }
}
