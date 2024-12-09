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

import io.github.fishlikewater.raiden.http.core.RequestWrap;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

/**
 * {@code AbstractHttpRequestClient}
 *
 * @author zhangxiang
 * @version 1.0.0
 * @since 2023/11/27
 */
@SuppressWarnings("unused")
public abstract class AbstractHttpRequestClient {

    /**
     * 异步请求
     *
     * @param requestWrap 请求封装
     * @param <T>         返回类型
     * @return CompletableFuture<HttpResponse < T>>
     */
    public abstract <T> CompletableFuture<HttpResponse<T>> requestAsync(RequestWrap requestWrap);

    /**
     * 同步请求
     *
     * @param requestWrap 请求封装
     * @return <T> 返回类型
     */
    public abstract <T> HttpResponse<T> requestSync(RequestWrap requestWrap) throws IOException, InterruptedException;

    /**
     * 构建HttpRequest
     *
     * @param requestWrap 请求封装
     */
    public abstract void buildHttpRequest(RequestWrap requestWrap);

    //--------------------get------------------------

    /**
     * get请求
     *
     * @param requestWrap 请求封装
     * @param <T>         返回类型
     * @return CompletableFuture
     */
    public abstract <T> CompletableFuture<HttpResponse<T>> getAsync(RequestWrap requestWrap);


    /**
     * get请求
     *
     * @param requestWrap 请求封装
     * @return <T> 返回类型
     */
    public abstract <T> HttpResponse<T> getSync(RequestWrap requestWrap) throws IOException, InterruptedException;

    //--------------------delete------------------------

    /**
     * delete请求
     *
     * @param requestWrap 请求封装
     * @return CompletableFuture
     */
    public abstract <T> CompletableFuture<HttpResponse<T>> deleteAsync(RequestWrap requestWrap);

    /**
     * delete请求
     *
     * @param requestWrap 请求封装
     * @return <T> 返回类型
     */
    public abstract <T> HttpResponse<T> deleteSync(RequestWrap requestWrap) throws IOException, InterruptedException;

    //--------------------post------------------------

    /**
     * post请求
     *
     * @param requestWrap 请求封装
     * @return CompletableFuture
     */
    public abstract <T> CompletableFuture<HttpResponse<T>> postAsync(RequestWrap requestWrap);

    /**
     * post请求
     *
     * @param requestWrap 请求封装
     * @param <T>         返回类型
     * @return <T> 返回类型
     */
    public abstract <T> HttpResponse<T> postSync(RequestWrap requestWrap) throws IOException, InterruptedException;

    //--------------------put------------------------

    /**
     * put请求
     *
     * @param requestWrap 请求封装
     * @return CompletableFuture
     */
    public abstract <T> CompletableFuture<HttpResponse<T>> putAsync(RequestWrap requestWrap);

    /**
     * put请求
     *
     * @param requestWrap 请求封装
     * @return <T> 返回类型
     */
    public abstract <T> HttpResponse<T> putSync(RequestWrap requestWrap) throws IOException, InterruptedException;

    //--------------------patch------------------------

    /**
     * patch请求
     *
     * @param requestWrap 请求封装
     * @return CompletableFuture
     */
    public abstract <T> CompletableFuture<HttpResponse<T>> patchAsync(RequestWrap requestWrap);

    /**
     * patch请求
     *
     * @param requestWrap 请求封装
     * @return <T> 返回类型
     */
    public abstract <T> HttpResponse<T> patchSync(RequestWrap requestWrap) throws IOException, InterruptedException;

    //--------------------File------------------------

    /**
     * 文件上传
     *
     * @param requestWrap 请求封装
     * @return CompletableFuture
     */
    public abstract <T> CompletableFuture<HttpResponse<T>> fileAsync(RequestWrap requestWrap);

    /**
     * 文件上传
     *
     * @param requestWrap 请求封装
     * @return <T> 返回类型
     */
    public abstract <T> HttpResponse<T> fileSync(RequestWrap requestWrap) throws IOException, InterruptedException;

    //--------------------form------------------------

    /**
     * form表单上传
     *
     * @param requestWrap 请求封装
     * @return CompletableFuture
     */
    public abstract <T> CompletableFuture<HttpResponse<T>> formAsync(RequestWrap requestWrap);

    /**
     * form表单上传
     *
     * @param requestWrap 请求封装
     * @return <T> 返回类型
     */
    public abstract <T> HttpResponse<T> formSync(RequestWrap requestWrap) throws IOException, InterruptedException;
}
