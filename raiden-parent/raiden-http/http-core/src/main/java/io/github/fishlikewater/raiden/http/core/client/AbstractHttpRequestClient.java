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
     * @return CompletableFuture
     */
    public abstract <T> CompletableFuture<T> requestAsync(RequestWrap requestWrap);

    /**
     * 同步请求
     *
     * @param requestWrap 请求封装
     * @return <T> 返回类型
     */
    public abstract <T> T requestSync(RequestWrap requestWrap);

    //--------------------get------------------------

    /**
     * get请求
     *
     * @param requestWrap 请求封装
     * @param <T>         返回类型
     * @return CompletableFuture
     */
    public abstract <T> CompletableFuture<T> getAsync(RequestWrap requestWrap);


    /**
     * get请求
     *
     * @param requestWrap 请求封装
     * @return <T> 返回类型
     */
    public abstract <T> T getSync(RequestWrap requestWrap);

    //--------------------delete------------------------

    /**
     * delete请求
     *
     * @param requestWrap 请求封装
     * @return CompletableFuture
     */
    public abstract <T> CompletableFuture<T> deleteAsync(RequestWrap requestWrap);

    /**
     * delete请求
     *
     * @param requestWrap 请求封装
     * @return <T> 返回类型
     */
    public abstract <T> T deleteSync(RequestWrap requestWrap);

    //--------------------post------------------------

    /**
     * post请求
     *
     * @param requestWrap 请求封装
     * @return CompletableFuture
     */
    public abstract <T> CompletableFuture<T> postAsync(RequestWrap requestWrap);

    /**
     * post请求
     *
     * @param requestWrap 请求封装
     * @param <T>         返回类型
     * @return <T> 返回类型
     */
    public abstract <T> T postSync(RequestWrap requestWrap);

    //--------------------put------------------------

    /**
     * put请求
     *
     * @param requestWrap 请求封装
     * @return CompletableFuture
     */
    public abstract <T> CompletableFuture<T> putAsync(RequestWrap requestWrap);

    /**
     * put请求
     *
     * @param requestWrap 请求封装
     * @return <T> 返回类型
     */
    public abstract <T> T putSync(RequestWrap requestWrap);

    //--------------------patch------------------------

    /**
     * patch请求
     *
     * @param requestWrap 请求封装
     * @return CompletableFuture
     */
    public abstract <T> CompletableFuture<T> patchAsync(RequestWrap requestWrap);

    /**
     * patch请求
     *
     * @param requestWrap 请求封装
     * @return <T> 返回类型
     */
    public abstract <T> T patchSync(RequestWrap requestWrap);

    //--------------------File------------------------

    /**
     * 文件上传
     *
     * @param requestWrap 请求封装
     * @return CompletableFuture
     */
    public abstract <T> CompletableFuture<T> fileAsync(RequestWrap requestWrap);

    /**
     * 文件上传
     *
     * @param requestWrap 请求封装
     * @return <T> 返回类型
     */
    public abstract <T> T fileSync(RequestWrap requestWrap);

    //--------------------form------------------------

    /**
     * form表单上传
     *
     * @param requestWrap 请求封装
     * @return CompletableFuture
     */
    public abstract <T> CompletableFuture<T> formAsync(RequestWrap requestWrap);

    /**
     * form表单上传
     *
     * @param requestWrap 请求封装
     * @return <T> 返回类型
     */
    public abstract <T> T formSync(RequestWrap requestWrap);
}
