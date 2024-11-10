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
package io.github.fishlikewater.raiden.http.core.client;

import cn.hutool.core.bean.BeanUtil;
import io.github.fishlikewater.raiden.core.Assert;
import io.github.fishlikewater.raiden.core.ObjectUtils;
import io.github.fishlikewater.raiden.core.StringUtils;
import io.github.fishlikewater.raiden.http.core.HttpBootStrap;
import io.github.fishlikewater.raiden.http.core.MultipartData;
import io.github.fishlikewater.raiden.http.core.RequestWrap;
import io.github.fishlikewater.raiden.http.core.constant.HttpConstants;
import io.github.fishlikewater.raiden.http.core.convert.MultiFileBodyProvider;
import io.github.fishlikewater.raiden.http.core.convert.ResponseJsonHandlerSubscriber;
import io.github.fishlikewater.raiden.http.core.enums.HttpMethod;
import io.github.fishlikewater.raiden.http.core.exception.HttpExceptionCheck;
import io.github.fishlikewater.raiden.http.core.interceptor.HttpClientInterceptor;
import io.github.fishlikewater.raiden.json.core.JSONUtils;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;

/**
 * {@code HttpRequestClient}
 *
 * @author zhangxiang
 * @version 1.0.0
 * @since 2023/11/27
 */
@Slf4j
public class HttpRequestClient extends AbstractHttpRequestClient {

    @Override
    public <T> CompletableFuture<T> requestAsync(RequestWrap requestWrap) {
        if (requestWrap.isForm()) {
            return this.formAsync(requestWrap);
        }
        MultipartData multipartData = requestWrap.getMultipartData();
        if (Objects.nonNull(multipartData) && !multipartData.isFileDownload()) {
            return this.fileAsync(requestWrap);
        }
        return this.requestAsyncSelector(requestWrap);
    }

    @Override
    public <T> T requestSync(RequestWrap requestWrap) {
        if (requestWrap.isForm()) {
            return this.formSync(requestWrap);
        }
        MultipartData multipartData = requestWrap.getMultipartData();
        if (Objects.nonNull(multipartData) && !multipartData.isFileDownload()) {
            return this.fileSync(requestWrap);
        }
        return this.requestSyncSelector(requestWrap);
    }

    private <T> CompletableFuture<T> requestAsyncSelector(RequestWrap requestWrap) {
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

    private <T> T requestSyncSelector(RequestWrap requestWrap) {
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
    public <T> CompletableFuture<T> getAsync(RequestWrap requestWrap) {
        this.checkHttpMethod(requestWrap, HttpMethod.GET);
        HttpRequest httpRequest = getHttpRequest(requestWrap);
        return handlerAsync(requestWrap, httpRequest);
    }

    @Override
    public <T> T getSync(RequestWrap requestWrap) {
        this.checkHttpMethod(requestWrap, HttpMethod.GET);
        HttpRequest httpRequest = getHttpRequest(requestWrap);
        return handlerSync(requestWrap, httpRequest);
    }

    @Override
    public <T> CompletableFuture<T> deleteAsync(RequestWrap requestWrap) {
        this.checkHttpMethod(requestWrap, HttpMethod.DELETE);
        HttpRequest httpRequest = getHttpRequest(requestWrap);
        return handlerAsync(requestWrap, httpRequest);
    }

    @Override
    public <T> T deleteSync(RequestWrap requestWrap) {
        this.checkHttpMethod(requestWrap, HttpMethod.DELETE);
        HttpRequest httpRequest = getHttpRequest(requestWrap);
        return handlerSync(requestWrap, httpRequest);
    }

    @Override
    public <T> CompletableFuture<T> postAsync(RequestWrap requestWrap) {
        this.checkHttpMethod(requestWrap, HttpMethod.POST);
        HttpRequest httpRequest = getHttpRequestBody(requestWrap);
        return handlerAsync(requestWrap, httpRequest);
    }

    @Override
    public <T> T postSync(RequestWrap requestWrap) {
        this.checkHttpMethod(requestWrap, HttpMethod.POST);
        HttpRequest httpRequest = getHttpRequestBody(requestWrap);
        return handlerSync(requestWrap, httpRequest);
    }

    @Override
    public <T> CompletableFuture<T> putAsync(RequestWrap requestWrap) {
        this.checkHttpMethod(requestWrap, HttpMethod.PUT);
        HttpRequest httpRequest = getHttpRequestBody(requestWrap);
        return handlerAsync(requestWrap, httpRequest);
    }

    @Override
    public <T> T putSync(RequestWrap requestWrap) {
        this.checkHttpMethod(requestWrap, HttpMethod.PUT);
        HttpRequest httpRequest = getHttpRequestBody(requestWrap);
        return handlerSync(requestWrap, httpRequest);
    }

    @Override
    public <T> CompletableFuture<T> patchAsync(RequestWrap requestWrap) {
        this.checkHttpMethod(requestWrap, HttpMethod.PATCH);
        HttpRequest httpRequest = getHttpRequestBody(requestWrap);
        return handlerAsync(requestWrap, httpRequest);
    }

    @Override
    public <T> T patchSync(RequestWrap requestWrap) {
        this.checkHttpMethod(requestWrap, HttpMethod.PATCH);
        HttpRequest httpRequest = getHttpRequestBody(requestWrap);
        return handlerSync(requestWrap, httpRequest);
    }

    @Override
    public <T> CompletableFuture<T> fileAsync(RequestWrap requestWrap) {
        HttpRequest httpRequest = getFileHttpRequest(requestWrap);
        return handlerAsync(requestWrap, httpRequest);
    }

    @Override
    public <T> T fileSync(RequestWrap requestWrap) {
        HttpRequest httpRequest = getFileHttpRequest(requestWrap);
        return handlerSync(requestWrap, httpRequest);
    }

    @Override
    public <T> CompletableFuture<T> formAsync(RequestWrap requestWrap) {
        HttpRequest httpRequest = getFormHttpRequest(requestWrap);
        return handlerAsync(requestWrap, httpRequest);
    }

    @Override
    public <T> T formSync(RequestWrap requestWrap) {
        HttpRequest httpRequest = getFormHttpRequest(requestWrap);
        return handlerSync(requestWrap, httpRequest);
    }

    // ---------------------------------------------------------------- private

    private void checkHttpMethod(RequestWrap requestWrap, HttpMethod httpMethod) {
        if (ObjectUtils.notEquals(requestWrap.getHttpMethod(), httpMethod)) {
            HttpExceptionCheck.INSTANCE.throwUnchecked("The current calling method only supports {}", httpMethod.name());
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
        requestWrap.setHttpRequest(httpRequest);
        requestBefore(requestWrap);
        return requestWrap.getHttpRequest();
    }

    private HttpRequest getHttpRequestBody(RequestWrap requestWrap) {
        String body = Objects.isNull(requestWrap.getBodyObject()) ? "" : JSONUtils.HutoolJSON.toJsonStr(requestWrap.getBodyObject());
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
        requestWrap.setHttpRequest(httpRequest);
        requestBefore(requestWrap);
        return requestWrap.getHttpRequest();
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
        requestWrap.setHttpRequest(httpRequest);
        requestBefore(requestWrap);
        return requestWrap.getHttpRequest();
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
        requestWrap.setHttpRequest(httpRequest);
        requestBefore(requestWrap);
        return requestWrap.getHttpRequest();
    }

    @SuppressWarnings("unchecked")
    private <T> CompletableFuture<T> handlerAsync(RequestWrap requestWrap, HttpRequest httpRequest) {
        Class<?> typeArgumentClass = requestWrap.getTypeArgumentClass();
        MultipartData multipartData = requestWrap.getMultipartData();
        HttpClient httpClient = requestWrap.getHttpClient();
        if (ObjectUtils.isNullOrEmpty(httpClient)) {
            httpClient = HttpBootStrap.getHttpClient(HttpConstants.DEFAULT);
        }
        if (typeArgumentClass.isAssignableFrom(byte[].class)) {
            CompletableFuture<byte[]> completableFuture = httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofByteArray())
                    .thenApply(res -> requestAfter(res, requestWrap).body());
            return (CompletableFuture<T>) completableFuture;
        }
        if (typeArgumentClass.isAssignableFrom(Path.class) && Objects.nonNull(multipartData)) {
            final Path path = multipartData.getPath();
            Assert.notNull(path, "Please pass in the file save path");
            if (path.toFile().isDirectory()) {
                return this.downFile(requestWrap, httpRequest, httpClient, path, multipartData);
            } else {
                return this.uploadFile(requestWrap, httpRequest, httpClient, path, multipartData);
            }
        }
        return this.jsonAsync(requestWrap, httpRequest, httpClient, typeArgumentClass);
    }

    private <T> CompletableFuture<T> jsonAsync(RequestWrap requestWrap, HttpRequest httpRequest, HttpClient httpClient, Class<?> typeArgumentClass) {
        return httpClient
                .sendAsync(httpRequest, (responseInfo) -> new ResponseJsonHandlerSubscriber<T>(responseInfo.headers(), typeArgumentClass))
                .thenApply(res -> requestAfter(res, requestWrap).body())
                .handle(this.throwableFunction(requestWrap));
    }

    @SuppressWarnings("unchecked")
    private <T> CompletableFuture<T> uploadFile(RequestWrap requestWrap, HttpRequest httpRequest, HttpClient httpClient, Path path, MultipartData multipartData) {
        return (CompletableFuture<T>) httpClient
                .sendAsync(httpRequest, HttpResponse.BodyHandlers.ofFile(path, multipartData.getOpenOptions()))
                .thenApply(res -> requestAfter(res, requestWrap).body())
                .handle(this.throwableFunction(requestWrap));
    }

    @SuppressWarnings("unchecked")
    private <T> CompletableFuture<T> downFile(RequestWrap requestWrap, HttpRequest httpRequest, HttpClient httpClient, Path path, MultipartData multipartData) {
        return (CompletableFuture<T>) httpClient
                .sendAsync(httpRequest, HttpResponse.BodyHandlers.ofFileDownload(path, multipartData.getOpenOptions()))
                .thenApply(res -> requestAfter(res, requestWrap).body())
                .handle(this.throwableFunction(requestWrap));
    }

    private <T> BiFunction<T, Throwable, T> throwableFunction(RequestWrap requestWrap) {
        return (res, ex) -> {
            if (ObjectUtils.isNullOrEmpty(ex)) {
                return res;
            }
            requestWrap.getExceptionProcessor().exceptionHandle(requestWrap, ex);
            return null;
        };
    }

    private <T> T handlerSync(RequestWrap requestWrap, HttpRequest httpRequest) {
        Class<?> returnType = requestWrap.getReturnType();
        HttpClient httpClient = requestWrap.getHttpClient();
        MultipartData multipartData = requestWrap.getMultipartData();
        if (ObjectUtils.isNullOrEmpty(httpClient)) {
            httpClient = HttpBootStrap.getHttpClient(HttpConstants.DEFAULT);
            requestWrap.setHttpClient(httpClient);
        }
        if (returnType.isAssignableFrom(byte[].class)) {
            return handleReturnBytes(requestWrap, httpRequest);
        }
        if (returnType.isAssignableFrom(Path.class) && Objects.nonNull(multipartData)) {
            return handleFile(requestWrap, httpRequest);
        }
        return this.handleJson(requestWrap, httpRequest);
    }

    private <T> T handleJson(RequestWrap requestWrap, HttpRequest httpRequest) {
        try {
            HttpResponse<T> response = requestWrap.getHttpClient().send(httpRequest, (responseInfo) -> new ResponseJsonHandlerSubscriber<>(responseInfo.headers(), requestWrap.getReturnType()));
            return this.requestAfter(response, requestWrap).body();
        } catch (Exception e) {
            requestWrap.getExceptionProcessor().exceptionHandle(requestWrap, e);
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    private <T> T handleFile(RequestWrap requestWrap, HttpRequest httpRequest) {
        MultipartData multipartData = requestWrap.getMultipartData();
        final Path path = multipartData.getPath();
        Assert.notNull(path, "Please pass in the file save path");
        HttpResponse<Path> response;
        try {
            if (path.toFile().isDirectory()) {
                response = requestWrap.getHttpClient().send(httpRequest, HttpResponse.BodyHandlers.ofFileDownload(path, multipartData.getOpenOptions()));
            } else {
                response = requestWrap.getHttpClient().send(httpRequest, HttpResponse.BodyHandlers.ofFile(path, multipartData.getOpenOptions()));
            }
            return requestAfter((HttpResponse<T>) response, requestWrap).body();
        } catch (Exception e) {
            requestWrap.getExceptionProcessor().exceptionHandle(requestWrap, e);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private <T> T handleReturnBytes(RequestWrap requestWrap, HttpRequest httpRequest) {
        try {
            HttpResponse<byte[]> response = requestWrap.getHttpClient().send(httpRequest, HttpResponse.BodyHandlers.ofByteArray());
            return requestAfter((HttpResponse<T>) response, requestWrap).body();
        } catch (Exception e) {
            requestWrap.getExceptionProcessor().exceptionHandle(requestWrap, e);
        }
        return null;
    }

    private void requestBefore(RequestWrap requestWrap) {
        List<HttpClientInterceptor> interceptors = requestWrap.getInterceptors();
        if (ObjectUtils.isNullOrEmpty(interceptors)) {
            return;
        }
        for (HttpClientInterceptor interceptor : interceptors) {
            interceptor.requestBefore(requestWrap);
        }
    }

    private <T> HttpResponse<T> requestAfter(HttpResponse<T> response, RequestWrap requestWrap) {
        if (ObjectUtils.notEquals(response.statusCode(), HttpConstants.HTTP_OK)) {
            requestWrap.getExceptionProcessor().invalidRespHandle(requestWrap, response);
        }
        if (Objects.nonNull(requestWrap.getInterceptors())) {
            for (HttpClientInterceptor interceptor : requestWrap.getInterceptors()) {
                response = interceptor.requestAfter(requestWrap, response);
            }
        }
        return response;
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
                    HttpExceptionCheck.INSTANCE.throwUnchecked(e, "join params error");
                }
            }
            return newUrl.toString();
        }
    }
}
