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
package com.github.fishlikewater.raiden.http.core;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.json.JSONUtil;
import com.github.fishlikewater.raiden.http.core.enums.HttpMethod;
import com.github.fishlikewater.raiden.http.core.interceptor.HttpClientInterceptor;
import com.github.fishlikewater.raiden.http.core.interceptor.LogInterceptor;
import com.github.fishlikewater.raiden.http.core.processor.MultiFileBodyProvider;
import com.github.fishlikewater.raiden.http.core.processor.ResponseJsonHandlerSubscriber;
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
 * @since 2023/11/27
 * @version 1.0.0
 */
@Slf4j
public class HttpRequestClient extends AbstractHttpRequestClient {

    private static final LogInterceptor LOG_INTERCEPTOR = new LogInterceptor();

    @Override
    public <T> CompletableFuture<T> getAsync(String url,
                                             Map<String, String> headMap,
                                             Map<String, String> paramMap,
                                             Class<T> typeArgument,
                                             HttpClientInterceptor interceptor,
                                             HttpClient httpClient,
                                             MultipartData multipartData) {
        if (Objects.isNull(httpClient)) {
            httpClient = HttpBootStrap.getHttpClient("default");
        }
        HttpRequest httpRequest = getHttpRequest(HttpMethod.GET, url, headMap, paramMap, interceptor);
        return handlerAsync(httpRequest, typeArgument, interceptor, multipartData, httpClient);
    }

    @Override
    public <T> CompletableFuture<T> getAsync(String url,
                                             Map<String, String> headMap,
                                             Map<String, String> paramMap,
                                             Class<T> typeArgument,
                                             HttpClientInterceptor interceptor,
                                             HttpClient httpClient) {
        return getAsync(url, headMap, paramMap, typeArgument, null, httpClient, null);
    }

    @Override
    public <T> CompletableFuture<T> getAsync(String url,
                                             Map<String, String> headMap,
                                             Map<String, String> paramMap,
                                             Class<T> typeArgument,
                                             HttpClient httpClient) {
        return getAsync(url, headMap, paramMap, typeArgument, null, httpClient);
    }

    @Override
    public <T> CompletableFuture<T> getAsync(String url, Class<T> typeArgument) {
        return getAsync(url, null, null, typeArgument, null, null);
    }

    @Override
    public <T> T getSync(String url,
                         Map<String, String> headMap,
                         Map<String, String> paramMap,
                         Class<T> returnType,
                         HttpClientInterceptor interceptor,
                         HttpClient httpClient,
                         MultipartData multipartData) throws IOException, InterruptedException {
        if (Objects.isNull(httpClient)) {
            httpClient = HttpBootStrap.getHttpClient("default");
        }
        HttpRequest httpRequest = getHttpRequest(HttpMethod.GET, url, headMap, paramMap, interceptor);
        return handlerSync(httpRequest, returnType, interceptor, multipartData, httpClient);
    }

    @Override
    public <T> T getSync(String url,
                         Map<String, String> headMap,
                         Map<String, String> paramMap,
                         Class<T> returnType,
                         HttpClientInterceptor interceptor,
                         HttpClient httpClient) throws IOException, InterruptedException {
        return getSync(url, headMap, paramMap, returnType, null, httpClient, null);
    }

    @Override
    public <T> T getSync(String url,
                         Map<String, String> headMap,
                         Map<String, String> paramMap,
                         Class<T> returnType,
                         HttpClient httpClient) throws IOException, InterruptedException {
        return getSync(url, headMap, paramMap, returnType, null, httpClient);
    }

    @Override
    public <T> T getSync(String url, Class<T> returnType) throws IOException, InterruptedException {
        return getSync(url, null, null, returnType, null, null);
    }

    @Override
    public <T> CompletableFuture<T> deleteAsync(String url,
                                                Map<String, String> headMap,
                                                Map<String, String> paramMap,
                                                Class<T> typeArgument,
                                                HttpClientInterceptor interceptor,
                                                HttpClient httpClient) {
        if (Objects.isNull(httpClient)) {
            httpClient = HttpBootStrap.getHttpClient("default");
        }
        HttpRequest httpRequest = getHttpRequest(HttpMethod.DELETE, url, headMap, paramMap, interceptor);
        return handlerAsync(httpRequest, typeArgument, interceptor, null, httpClient);
    }

    @Override
    public <T> CompletableFuture<T> deleteAsync(String url,
                                                Map<String, String> headMap,
                                                Map<String, String> paramMap,
                                                Class<T> typeArgument,
                                                HttpClient httpClient) {
        return deleteAsync(url, headMap, paramMap, typeArgument, null, httpClient);
    }

    @Override
    public <T> CompletableFuture<T> deleteAsync(String url, Class<T> returnType) {
        return deleteAsync(url, null, null, returnType, null, null);
    }

    @Override
    public <T> T deleteSync(String url,
                            Map<String, String> headMap,
                            Map<String, String> paramMap,
                            Class<T> returnType,
                            HttpClientInterceptor interceptor,
                            HttpClient httpClient) throws IOException, InterruptedException {
        if (Objects.isNull(httpClient)) {
            httpClient = HttpBootStrap.getHttpClient("default");
        }
        HttpRequest httpRequest = getHttpRequest(HttpMethod.DELETE, url, headMap, paramMap, interceptor);
        return handlerSync(httpRequest, returnType, interceptor, null, httpClient);
    }

    @Override
    public <T> T deleteSync(String url,
                            Map<String, String> headMap,
                            Map<String, String> paramMap,
                            Class<T> returnType,
                            HttpClient httpClient) throws IOException, InterruptedException {
        return deleteSync(url, headMap, paramMap, returnType, null, httpClient);
    }

    @Override
    public <T> T deleteSync(String url, Class<T> returnType) throws IOException, InterruptedException {
        return deleteSync(url, null, null, returnType, null, null);
    }

    @Override
    public <T> CompletableFuture<T> postAsync(String url,
                                              Map<String, String> headMap,
                                              Object bodyObject,
                                              Class<T> typeArgument,
                                              HttpClientInterceptor interceptor,
                                              HttpClient httpClient) {
        if (Objects.isNull(httpClient)) {
            httpClient = HttpBootStrap.getHttpClient("default");
        }
        HttpRequest httpRequest = getHttpRequest(HttpMethod.POST, url, headMap, bodyObject, interceptor);
        return handlerAsync(httpRequest, typeArgument, interceptor, null, httpClient);
    }

    @Override
    public <T> CompletableFuture<T> postAsync(String url,
                                              Map<String, String> headMap,
                                              Object bodyObject,
                                              Class<T> typeArgument,
                                              HttpClient httpClient) {
        return postAsync(url, headMap, bodyObject, typeArgument, null, httpClient);
    }

    @Override
    public <T> CompletableFuture<T> postAsync(String url,
                                              Object bodyObject,
                                              Class<T> typeArgument) {
        return postAsync(url, null, bodyObject, typeArgument, null, null);
    }

    @Override
    public <T> T postSync(String url,
                          Map<String, String> headMap,
                          Object bodyObject,
                          Class<T> returnType,
                          HttpClientInterceptor interceptor,
                          HttpClient httpClient) throws IOException, InterruptedException {
        if (Objects.isNull(httpClient)) {
            httpClient = HttpBootStrap.getHttpClient("default");
        }
        HttpRequest httpRequest = getHttpRequest(HttpMethod.POST, url, headMap, bodyObject, interceptor);
        return handlerSync(httpRequest, returnType, interceptor, null, httpClient);
    }

    @Override
    public <T> T postSync(String url,
                          Map<String, String> headMap,
                          Object bodyObject,
                          Class<T> returnType,
                          HttpClient httpClient) throws IOException, InterruptedException {
        return postSync(url, headMap, bodyObject, returnType, null, httpClient);
    }

    @Override
    public <T> T postSync(String url,
                          Object bodyObject,
                          Class<T> returnType) throws IOException, InterruptedException {
        return postSync(url, null, bodyObject, returnType, null, null);
    }

    @Override
    public <T> CompletableFuture<T> putAsync(String url,
                                             Map<String, String> headMap,
                                             Object bodyObject,
                                             Class<T> typeArgument,
                                             HttpClientInterceptor interceptor,
                                             HttpClient httpClient) {
        if (Objects.isNull(httpClient)) {
            httpClient = HttpBootStrap.getHttpClient("default");
        }
        HttpRequest httpRequest = getHttpRequest(HttpMethod.PUT, url, headMap, bodyObject, interceptor);
        return handlerAsync(httpRequest, typeArgument, interceptor, null, httpClient);
    }

    @Override
    public <T> CompletableFuture<T> putAsync(String url,
                                             Map<String, String> headMap,
                                             Object bodyObject,
                                             Class<T> typeArgument,
                                             HttpClient httpClient) {
        return putAsync(url, headMap, bodyObject, typeArgument, null, httpClient);
    }

    @Override
    public <T> CompletableFuture<T> putAsync(String url,
                                             Object bodyObject,
                                             Class<T> typeArgument) {
        return putAsync(url, null, bodyObject, typeArgument, null, null);
    }

    @Override
    public <T> T putSync(String url,
                         Map<String, String> headMap,
                         Object bodyObject,
                         Class<T> returnType,
                         HttpClientInterceptor interceptor,
                         HttpClient httpClient) throws IOException, InterruptedException {
        if (Objects.isNull(httpClient)) {
            httpClient = HttpBootStrap.getHttpClient("default");
        }
        HttpRequest httpRequest = getHttpRequest(HttpMethod.PUT, url, headMap, bodyObject, interceptor);
        return handlerSync(httpRequest, returnType, interceptor, null, httpClient);
    }

    @Override
    public <T> T putSync(String url,
                         Map<String, String> headMap,
                         Object bodyObject,
                         Class<T> returnType,
                         HttpClient httpClient) throws IOException, InterruptedException {
        return putSync(url, headMap, bodyObject, returnType, null, httpClient);
    }

    @Override
    public <T> T putSync(String url,
                         Object bodyObject,
                         Class<T> returnType) throws IOException, InterruptedException {
        return putSync(url, null, bodyObject, returnType, null, null);
    }

    @Override
    public <T> CompletableFuture<T> patchAsync(String url,
                                               Map<String, String> headMap,
                                               Object bodyObject,
                                               Class<T> typeArgument,
                                               HttpClientInterceptor interceptor,
                                               HttpClient httpClient) {
        if (Objects.isNull(httpClient)) {
            httpClient = HttpBootStrap.getHttpClient("default");
        }
        HttpRequest httpRequest = getHttpRequest(HttpMethod.PATCH, url, headMap, bodyObject, interceptor);
        return handlerAsync(httpRequest, typeArgument, interceptor, null, httpClient);
    }

    @Override
    public <T> CompletableFuture<T> patchAsync(String url,
                                               Map<String, String> headMap,
                                               Object bodyObject,
                                               Class<T> typeArgument,
                                               HttpClient httpClient) {
        return patchAsync(url, headMap, bodyObject, typeArgument, null, httpClient);
    }

    @Override
    public <T> CompletableFuture<T> patchAsync(String url,
                                               Object bodyObject,
                                               Class<T> typeArgument) {
        return patchAsync(url, null, bodyObject, typeArgument, null, null);
    }

    @Override
    public <T> T patchSync(String url,
                           Map<String, String> headMap,
                           Object bodyObject,
                           Class<T> returnType,
                           HttpClientInterceptor interceptor,
                           HttpClient httpClient) throws IOException, InterruptedException {
        if (Objects.isNull(httpClient)) {
            httpClient = HttpBootStrap.getHttpClient("default");
        }
        HttpRequest httpRequest = getHttpRequest(HttpMethod.PATCH, url, headMap, bodyObject, interceptor);
        return handlerSync(httpRequest, returnType, interceptor, null, httpClient);
    }

    @Override
    public <T> T patchSync(String url,
                           Map<String, String> headMap,
                           Object bodyObject,
                           Class<T> returnType,
                           HttpClient httpClient) throws IOException, InterruptedException {
        return patchSync(url, headMap, bodyObject, returnType, null, httpClient);
    }

    @Override
    public <T> T patchSync(String url,
                           Object bodyObject,
                           Class<T> returnType) throws IOException, InterruptedException {
        return patchSync(url, null, bodyObject, returnType, null, null);
    }

    @Override
    public <T> CompletableFuture<T> fileAsync(HttpMethod method,
                                              String url,
                                              Map<String, String> headMap,
                                              Object bodyObject,
                                              Class<T> typeArgument,
                                              HttpClientInterceptor interceptor,
                                              MultipartData multipartData,
                                              HttpClient httpClient) {
        if (Objects.isNull(httpClient)) {
            httpClient = HttpBootStrap.getHttpClient("default");
        }
        HttpRequest httpRequest = getFileHttpRequest(method, url, headMap, bodyObject, multipartData, interceptor);
        return handlerAsync(httpRequest, typeArgument, interceptor, multipartData, httpClient);
    }

    @Override
    public <T> CompletableFuture<T> fileAsync(HttpMethod method,
                                              String url,
                                              Map<String, String> headMap,
                                              Object bodyObject,
                                              Class<T> typeArgument,
                                              MultipartData multipartData,
                                              HttpClient httpClient) {
        return fileAsync(method, url, headMap, bodyObject, typeArgument, null, multipartData, httpClient);
    }

    @Override
    public <T> T fileSync(HttpMethod method,
                          String url,
                          Map<String, String> headMap,
                          Object bodyObject,
                          Class<T> returnType,
                          HttpClientInterceptor interceptor,
                          MultipartData multipartData,
                          HttpClient httpClient) throws IOException, InterruptedException {
        if (Objects.isNull(httpClient)) {
            httpClient = HttpBootStrap.getHttpClient("default");
        }
        HttpRequest httpRequest = getFileHttpRequest(method, url, headMap, bodyObject, multipartData, interceptor);
        return handlerSync(httpRequest, returnType, interceptor, multipartData, httpClient);
    }

    @Override
    public <T> T fileSync(HttpMethod method,
                          String url,
                          Map<String, String> headMap,
                          Object bodyObject,
                          Class<T> returnType,
                          MultipartData multipartData,
                          HttpClient httpClient) throws IOException, InterruptedException {
        return fileSync(method, url, headMap, bodyObject, returnType, null, multipartData, httpClient);
    }

    @Override
    public <T> CompletableFuture<T> formAsync(HttpMethod method,
                                              String url,
                                              Map<String, String> headMap,
                                              Map<String, String> paramMap,
                                              Object bodyObject,
                                              Class<T> typeArgument,
                                              HttpClientInterceptor interceptor,
                                              HttpClient httpClient) {
        if (Objects.isNull(httpClient)) {
            httpClient = HttpBootStrap.getHttpClient("default");
        }
        HttpRequest httpRequest = getFormHttpRequest(method, url, headMap, paramMap, bodyObject, interceptor);
        return handlerAsync(httpRequest, typeArgument, interceptor, null, httpClient);
    }

    @Override
    public <T> CompletableFuture<T> formAsync(HttpMethod method,
                                              String url,
                                              Map<String, String> headMap,
                                              Map<String, String> paramMap,
                                              Object bodyObject, Class<T> typeArgument,
                                              HttpClient httpClient) {
        return formAsync(method, url, headMap, paramMap, bodyObject, typeArgument, null, httpClient);
    }

    @Override
    public <T> T formSync(HttpMethod method,
                          String url,
                          Map<String, String> headMap,
                          Map<String, String> paramMap,
                          Object bodyObject,
                          Class<T> returnType,
                          HttpClientInterceptor interceptor,
                          HttpClient httpClient) throws IOException, InterruptedException {
        if (Objects.isNull(httpClient)) {
            httpClient = HttpBootStrap.getHttpClient("default");
        }
        HttpRequest httpRequest = getFormHttpRequest(method, url, headMap, paramMap, bodyObject, interceptor);
        return handlerSync(httpRequest, returnType, interceptor, null, httpClient);
    }

    @Override
    public <T> T formSync(HttpMethod method,
                          String url,
                          Map<String, String> headMap,
                          Map<String, String> paramMap,
                          Object bodyObject,
                          Class<T> returnType,
                          HttpClient httpClient) throws IOException, InterruptedException {
        return formSync(method, url, headMap, paramMap, bodyObject, returnType, null, httpClient);
    }

    private HttpRequest getHttpRequest(HttpMethod method,
                                       String url,
                                       Map<String, String> headMap,
                                       Map<String, String> paramMap,
                                       HttpClientInterceptor interceptor) {
        final HttpRequest.Builder builder = HttpRequest.newBuilder().uri(URI.create(this.getRequestUrl(url, paramMap)));
        if (Objects.nonNull(headMap)) {
            headMap.forEach(builder::header);
        }
        if (Objects.requireNonNull(method) == HttpMethod.DELETE) {
            builder.DELETE();
        } else {
            builder.GET();
        }
        HttpRequest httpRequest = builder.build();
        httpRequest = requestBefore(interceptor, httpRequest);
        printLog(httpRequest);
        return httpRequest;
    }

    private HttpRequest getHttpRequest(HttpMethod method,
                                       String url,
                                       Map<String, String> headMap,
                                       Object bodyObject,
                                       HttpClientInterceptor interceptor) {
        HttpRequest.BodyPublisher requestBody = HttpRequest.BodyPublishers.ofString(Objects.isNull(bodyObject) ? "" : JSONUtil.toJsonStr(bodyObject));
        final HttpRequest.Builder builder = HttpRequest.newBuilder().method(method.name(), requestBody).uri(URI.create(url));
        if (Objects.nonNull(headMap)) {
            headMap.forEach(builder::header);
        }
        if (Objects.isNull(headMap.get("Content-Type"))) {
            builder.header("Content-Type", "application/json;charset=utf-8");
        }
        HttpRequest httpRequest = builder.build();
        httpRequest = requestBefore(interceptor, httpRequest);
        printLog(httpRequest);
        return httpRequest;
    }

    private HttpRequest getFormHttpRequest(HttpMethod method,
                                           String url,
                                           Map<String, String> headMap,
                                           Map<String, String> paramMap,
                                           Object bodyObject,
                                           HttpClientInterceptor interceptor) {
        if (Objects.nonNull(paramMap) && !paramMap.isEmpty()) {
            url = this.getRequestUrl(url, paramMap);
        }

        HttpRequest.BodyPublisher requestBody;
        StringBuilder params = new StringBuilder("rd=").append(Math.random());
        if (Objects.nonNull(bodyObject)) {
            if (bodyObject instanceof Map<?, ?> map) {
                for (Map.Entry<?, ?> item : map.entrySet()) {
                    String param = STR."&\{item.getKey().toString().trim()}=\{item.getValue().toString().trim()}";
                    params.append(param);
                }
            } else {
                final Map<String, Object> map = BeanUtil.beanToMap(bodyObject);
                for (Map.Entry<?, ?> item : map.entrySet()) {
                    String param = STR."&\{item.getKey().toString().trim()}=\{item.getValue().toString().trim()}";
                    params.append(param);
                }
            }
        }
        requestBody = HttpRequest.BodyPublishers.ofString(params.toString());
        final HttpRequest.Builder builder = HttpRequest.newBuilder().method(method.name(), requestBody).uri(URI.create(url));
        if (Objects.nonNull(headMap)) {
            headMap.forEach(builder::header);
        }
        if (Objects.isNull(headMap.get("Content-Type"))) {
            builder.header("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        }
        HttpRequest httpRequest = builder.build();
        httpRequest = requestBefore(interceptor, httpRequest);
        printLog(httpRequest);
        return httpRequest;
    }

    private HttpRequest getFileHttpRequest(HttpMethod method,
                                           String url,
                                           Map<String, String> headMap,
                                           Object bodyObject,
                                           MultipartData multipartData,
                                           HttpClientInterceptor interceptor) {
        final String boundaryString = boundaryString();
        final HttpRequest.Builder builder = HttpRequest.newBuilder().uri(URI.create(url));
        if (Objects.nonNull(headMap)) {
            headMap.forEach(builder::header);
        }
        builder.header("Content-Type", STR."multipart/form-data; boundary=\{boundaryString}");
        HttpRequest.BodyPublisher requestBody = new MultiFileBodyProvider(multipartData, bodyObject, boundaryString);
        builder.method(method.name(), requestBody);
        HttpRequest httpRequest = builder.build();
        httpRequest = requestBefore(interceptor, httpRequest);
        printLog(httpRequest);
        return httpRequest;
    }

    /**
     * 生成一个随机的boundary字符串
     *
     * @return {@link String}
     */
    private static String boundaryString() {
        return STR."Boundary\{System.currentTimeMillis()}";
    }

    @SuppressWarnings("unchecked")
    private <T> CompletableFuture<T> handlerAsync(HttpRequest httpRequest,
                                                  Class<T> typeArgumentClass,
                                                  HttpClientInterceptor interceptor,
                                                  MultipartData multipartData,
                                                  HttpClient httpClient) {

        if (typeArgumentClass.isAssignableFrom(byte[].class)) {
            CompletableFuture<byte[]> completableFuture = httpClient.sendAsync(
                    httpRequest,
                    HttpResponse.BodyHandlers.ofByteArray()).thenApply(res -> requestAfter(res, interceptor).body()
            );
            return (CompletableFuture<T>) completableFuture;
        }
        if (typeArgumentClass.isAssignableFrom(Path.class) && Objects.nonNull(multipartData)) {
            final Path path = multipartData.getPath();
            Assert.notNull(path, "请传入文件保存路径");
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
    private <T> T handlerSync(HttpRequest httpRequest,
                              Class<T> returnType,
                              HttpClientInterceptor interceptor,
                              MultipartData multipartData,
                              HttpClient httpClient) throws IOException, InterruptedException {

        if (returnType.isAssignableFrom(byte[].class)) {
            final HttpResponse<byte[]> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofByteArray());
            return requestAfter((HttpResponse<T>) response, interceptor).body();
        }
        if (returnType.isAssignableFrom(Path.class) && Objects.nonNull(multipartData)) {
            final Path path = multipartData.getPath();
            Assert.notNull(path, "请传入文件保存路径");
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
            if (!url.contains("?")) {
                newUrl.append("?rd=").append(Math.random());
            }
            for (Map.Entry<String, String> item : map.entrySet()) {
                try {
                    String param = STR."&\{item.getKey().trim()}=\{URLEncoder.encode(item.getValue().trim(), StandardCharsets.UTF_8)}";
                    newUrl.append(param);
                } catch (Exception e) {
                    log.error("拼接参数异常", e);
                }
            }
            return newUrl.toString();
        }
    }
}
