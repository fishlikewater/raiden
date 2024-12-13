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

import com.fasterxml.jackson.core.JsonProcessingException;
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
import io.github.fishlikewater.raiden.json.core.JSONUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
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

    @Override
    public <T> CompletableFuture<HttpResponse<T>> requestAsync(RequestWrap requestWrap) {
        if (requestWrap.isForm()) {
            return this.formAsync(requestWrap);
        }
        MultipartData multipartData = requestWrap.getMultipartData();
        if (Objects.nonNull(multipartData) && !multipartData.isFileDownload()) {
            return this.fileAsync(requestWrap);
        }
        this.toBuildHttpRequest(requestWrap);
        return this.handlerAsync(requestWrap);
    }

    @Override
    public <T> HttpResponse<T> requestSync(RequestWrap requestWrap) throws IOException, InterruptedException {
        if (requestWrap.isForm()) {
            return this.formSync(requestWrap);
        }
        MultipartData multipartData = requestWrap.getMultipartData();
        if (Objects.nonNull(multipartData) && !multipartData.isFileDownload()) {
            return this.fileSync(requestWrap);
        }
        this.toBuildHttpRequest(requestWrap);
        return this.handlerSync(requestWrap);
    }

    @Override
    public void buildHttpRequest(RequestWrap requestWrap) {
        this.toBuildHttpRequest(requestWrap);
    }

    @Override
    public <T> CompletableFuture<HttpResponse<T>> getAsync(RequestWrap requestWrap) {
        this.checkHttpMethod(requestWrap, HttpMethod.GET);
        this.getHttpRequest(requestWrap);
        return handlerAsync(requestWrap);
    }

    @Override
    public <T> HttpResponse<T> getSync(RequestWrap requestWrap) throws IOException, InterruptedException {
        this.checkHttpMethod(requestWrap, HttpMethod.GET);
        this.getHttpRequest(requestWrap);
        return handlerSync(requestWrap);
    }

    @Override
    public <T> CompletableFuture<HttpResponse<T>> deleteAsync(RequestWrap requestWrap) {
        this.checkHttpMethod(requestWrap, HttpMethod.DELETE);
        this.getHttpRequest(requestWrap);
        return handlerAsync(requestWrap);
    }

    @Override
    public <T> HttpResponse<T> deleteSync(RequestWrap requestWrap) throws IOException, InterruptedException {
        this.checkHttpMethod(requestWrap, HttpMethod.DELETE);
        this.getHttpRequest(requestWrap);
        return handlerSync(requestWrap);
    }

    @Override
    public <T> CompletableFuture<HttpResponse<T>> postAsync(RequestWrap requestWrap) {
        this.checkHttpMethod(requestWrap, HttpMethod.POST);
        this.getHttpRequestBody(requestWrap);
        return handlerAsync(requestWrap);
    }

    @Override
    public <T> HttpResponse<T> postSync(RequestWrap requestWrap) throws IOException, InterruptedException {
        this.checkHttpMethod(requestWrap, HttpMethod.POST);
        this.getHttpRequestBody(requestWrap);
        return handlerSync(requestWrap);
    }

    @Override
    public <T> CompletableFuture<HttpResponse<T>> putAsync(RequestWrap requestWrap) {
        this.checkHttpMethod(requestWrap, HttpMethod.PUT);
        this.getHttpRequestBody(requestWrap);
        return handlerAsync(requestWrap);
    }

    @Override
    public <T> HttpResponse<T> putSync(RequestWrap requestWrap) throws IOException, InterruptedException {
        this.checkHttpMethod(requestWrap, HttpMethod.PUT);
        this.getHttpRequestBody(requestWrap);
        return handlerSync(requestWrap);
    }

    @Override
    public <T> CompletableFuture<HttpResponse<T>> patchAsync(RequestWrap requestWrap) {
        this.checkHttpMethod(requestWrap, HttpMethod.PATCH);
        this.getHttpRequestBody(requestWrap);
        return handlerAsync(requestWrap);
    }

    @Override
    public <T> HttpResponse<T> patchSync(RequestWrap requestWrap) throws IOException, InterruptedException {
        this.checkHttpMethod(requestWrap, HttpMethod.PATCH);
        this.getHttpRequestBody(requestWrap);
        return handlerSync(requestWrap);
    }

    @Override
    public <T> CompletableFuture<HttpResponse<T>> fileAsync(RequestWrap requestWrap) {
        this.getFileHttpRequest(requestWrap);
        return handlerAsync(requestWrap);
    }

    @Override
    public <T> HttpResponse<T> fileSync(RequestWrap requestWrap) throws IOException, InterruptedException {
        this.getFileHttpRequest(requestWrap);
        return handlerSync(requestWrap);
    }

    @Override
    public <T> CompletableFuture<HttpResponse<T>> formAsync(RequestWrap requestWrap) {
        this.getFormHttpRequest(requestWrap);
        return handlerAsync(requestWrap);
    }

    @Override
    public <T> HttpResponse<T> formSync(RequestWrap requestWrap) throws IOException, InterruptedException {
        this.getFormHttpRequest(requestWrap);
        return handlerSync(requestWrap);
    }

    // ---------------------------------------------------------------- buildHttpRequest

    private void toBuildHttpRequest(RequestWrap requestWrap) {
        switch (requestWrap.getHttpMethod()) {
            case GET -> {
                this.checkHttpMethod(requestWrap, HttpMethod.GET);
                this.getHttpRequest(requestWrap);
            }
            case DELETE -> {
                this.checkHttpMethod(requestWrap, HttpMethod.DELETE);
                this.getHttpRequest(requestWrap);
            }
            case POST -> {
                this.checkHttpMethod(requestWrap, HttpMethod.POST);
                this.getHttpRequestBody(requestWrap);
            }
            case PUT -> {
                this.checkHttpMethod(requestWrap, HttpMethod.PUT);
                this.getHttpRequestBody(requestWrap);
            }
            case PATCH -> {
                this.checkHttpMethod(requestWrap, HttpMethod.PATCH);
                this.getHttpRequestBody(requestWrap);
            }
            default ->
                    HttpExceptionCheck.INSTANCE.throwUnchecked("not.support.request.method:{}", requestWrap.getHttpMethod());
        }
    }

    // ---------------------------------------------------------------- Check

    private void checkHttpMethod(RequestWrap requestWrap, HttpMethod httpMethod) {
        if (ObjectUtils.notEquals(requestWrap.getHttpMethod(), httpMethod)) {
            HttpExceptionCheck.INSTANCE.throwUnchecked("The current calling method only supports {}", httpMethod.name());
        }
    }

    // ---------------------------------------------------------------- build HttpRequest

    private void getHttpRequest(RequestWrap requestWrap) {
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
    }

    private void getHttpRequestBody(RequestWrap requestWrap) {
        String body = "";
        try {
            body = JSONUtils.JACKSON.writeValueAsString(requestWrap.getBodyObject());
        } catch (JsonProcessingException e) {
            log.error("raiden.http: request.body.jackson.serializable.fail");
            HttpExceptionCheck.INSTANCE.throwUnchecked(e);
        }
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
    }

    private void getFormHttpRequest(RequestWrap requestWrap) {
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
                final Map<String, Object> map = ObjectUtils.beanToMap(bodyObject, false);
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
    }

    private void getFileHttpRequest(RequestWrap requestWrap) {
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
    }

    // ---------------------------------------------------------------- Async

    @SuppressWarnings("all")
    private <T> CompletableFuture<HttpResponse<T>> handlerAsync(RequestWrap requestWrap) {
        Class<?> typeArgumentClass = requestWrap.getTypeArgumentClass();
        MultipartData multipartData = requestWrap.getMultipartData();
        HttpClient httpClient = requestWrap.getHttpClient();
        if (ObjectUtils.isNullOrEmpty(httpClient)) {
            requestWrap.setHttpClient(HttpBootStrap.getHttpClient(HttpConstants.DEFAULT));
        }
        if (typeArgumentClass.isAssignableFrom(byte[].class)) {
            return (CompletableFuture) this.byteAsync(requestWrap);
        }
        if (typeArgumentClass.isAssignableFrom(InputStream.class)) {
            return (CompletableFuture) this.streamAsync(requestWrap);
        }
        if (typeArgumentClass.isAssignableFrom(Path.class) && Objects.nonNull(multipartData)) {
            final Path path = multipartData.getPath();
            Assert.notNull(path, "Please pass in the file save path");
            if (multipartData.isFileDownload()) {
                return (CompletableFuture) this.downFile(requestWrap);
            } else {
                return (CompletableFuture) this.uploadFile(requestWrap);
            }
        }
        return this.jsonAsync(requestWrap);
    }

    private CompletableFuture<HttpResponse<InputStream>> streamAsync(RequestWrap requestWrap) {
        return requestWrap.getHttpClient()
                .sendAsync(requestWrap.getHttpRequest(), HttpResponse.BodyHandlers.ofInputStream());
    }

    private CompletableFuture<HttpResponse<byte[]>> byteAsync(RequestWrap requestWrap) {
        return requestWrap.getHttpClient()
                .sendAsync(requestWrap.getHttpRequest(), HttpResponse.BodyHandlers.ofByteArray());
    }

    private CompletableFuture<HttpResponse<Path>> downFile(RequestWrap requestWrap) {
        return requestWrap
                .getHttpClient()
                .sendAsync(
                        requestWrap.getHttpRequest(),
                        HttpResponse.BodyHandlers.ofFileDownload(requestWrap.getMultipartData().getPath(), requestWrap.getMultipartData().getOpenOptions()));
    }

    private CompletableFuture<HttpResponse<Path>> uploadFile(RequestWrap requestWrap) {
        return requestWrap
                .getHttpClient()
                .sendAsync(
                        requestWrap.getHttpRequest(),
                        HttpResponse.BodyHandlers.ofFile(requestWrap.getMultipartData().getPath(), requestWrap.getMultipartData().getOpenOptions()));

    }

    private <T> CompletableFuture<HttpResponse<T>> jsonAsync(RequestWrap requestWrap) {
        return requestWrap
                .getHttpClient()
                .sendAsync(
                        requestWrap.getHttpRequest(),
                        (responseInfo) -> new ResponseJsonHandlerSubscriber<>(responseInfo.headers(), requestWrap.getTypeArgumentClass()));
    }

    // ---------------------------------------------------------------- Sync

    @SuppressWarnings("all")
    private <T> HttpResponse<T> handlerSync(RequestWrap requestWrap) throws IOException, InterruptedException {
        Class<?> returnType = requestWrap.getReturnType();
        HttpClient httpClient = requestWrap.getHttpClient();
        MultipartData multipartData = requestWrap.getMultipartData();
        if (ObjectUtils.isNullOrEmpty(httpClient)) {
            httpClient = HttpBootStrap.getHttpClient(HttpConstants.DEFAULT);
            requestWrap.setHttpClient(httpClient);
        }

        if (returnType.isAssignableFrom(byte[].class)) {
            return (HttpResponse) handleReturnBytes(requestWrap);
        }
        if (returnType.isAssignableFrom(Path.class) && Objects.nonNull(multipartData)) {
            return (HttpResponse) handleFile(requestWrap);
        }

        if (returnType.isAssignableFrom(InputStream.class)) {
            return (HttpResponse) handleStream(requestWrap);
        }
        return this.handleJson(requestWrap);

    }

    private HttpResponse<InputStream> handleStream(RequestWrap requestWrap) throws IOException, InterruptedException {
        return requestWrap.getHttpClient().send(requestWrap.getHttpRequest(), HttpResponse.BodyHandlers.ofInputStream());
    }

    private <T> HttpResponse<T> handleJson(RequestWrap requestWrap) throws IOException, InterruptedException {
        return requestWrap
                .getHttpClient()
                .send(requestWrap.getHttpRequest(), (responseInfo) -> new ResponseJsonHandlerSubscriber<>(responseInfo.headers(), requestWrap.getReturnType()));
    }

    private HttpResponse<Path> handleFile(RequestWrap requestWrap) throws IOException, InterruptedException {

        MultipartData multipartData = requestWrap.getMultipartData();
        final Path path = multipartData.getPath();
        Assert.notNull(path, "Please pass in the file save path");
        if (multipartData.isFileDownload()) {
            return requestWrap.getHttpClient().send(requestWrap.getHttpRequest(), HttpResponse.BodyHandlers.ofFileDownload(path, multipartData.getOpenOptions()));
        } else {
            return requestWrap.getHttpClient().send(requestWrap.getHttpRequest(), HttpResponse.BodyHandlers.ofFile(path, multipartData.getOpenOptions()));
        }
    }

    private HttpResponse<byte[]> handleReturnBytes(RequestWrap requestWrap) throws IOException, InterruptedException {
        return requestWrap.getHttpClient().send(requestWrap.getHttpRequest(), HttpResponse.BodyHandlers.ofByteArray());
    }

    // ---------------------------------------------------------------- Others

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

    /**
     * 生成一个随机的boundary字符串
     *
     * @return {@link String}
     */
    private String boundaryString() {
        return StringUtils.format("Boundary {}", System.currentTimeMillis());
    }

}
