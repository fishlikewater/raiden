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

import com.github.fishlikewater.raiden.http.core.enums.HttpMethod;
import com.github.fishlikewater.raiden.http.core.interceptor.HttpClientInterceptor;

import java.io.IOException;
import java.net.http.HttpClient;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * {@code AbstractHttpRequestClient}
 *
 * @author zhangxiang
 * @since 2023/11/27
 * @version 1.0.0
 */
public abstract class AbstractHttpRequestClient {

    //--------------------get------------------------

    /**
     * get请求
     *
     * @param url           请求地址
     * @param headMap       请求头
     * @param paramMap      请求参数
     * @param typeArgument  返回类型
     * @param interceptor   拦截器
     * @param httpClient    http客户端
     * @param multipartData 文件上传
     * @param <T>           返回类型
     * @return CompletableFuture
     */
    abstract <T> CompletableFuture<T> getAsync(String url,
                                               Map<String, String> headMap,
                                               Map<String, String> paramMap,
                                               Class<T> typeArgument,
                                               HttpClientInterceptor interceptor,
                                               HttpClient httpClient,
                                               MultipartData multipartData);

    /**
     * get请求
     *
     * @param url          请求地址
     * @param headMap      请求头
     * @param paramMap     请求参数
     * @param typeArgument 返回类型
     * @param interceptor  拦截器
     * @param httpClient   http客户端
     * @param <T>          返回类型
     * @return CompletableFuture
     */
    abstract <T> CompletableFuture<T> getAsync(String url,
                                               Map<String, String> headMap,
                                               Map<String, String> paramMap,
                                               Class<T> typeArgument,
                                               HttpClientInterceptor interceptor,
                                               HttpClient httpClient);

    /**
     * get请求
     *
     * @param url          请求地址
     * @param headMap      请求头
     * @param paramMap     请求参数
     * @param typeArgument 返回类型
     * @param httpClient   http客户端
     * @return CompletableFuture
     */
    abstract <T> CompletableFuture<T> getAsync(String url,
                                               Map<String, String> headMap,
                                               Map<String, String> paramMap,
                                               Class<T> typeArgument,
                                               HttpClient httpClient);

    /**
     * get请求
     *
     * @param url          请求地址
     * @param typeArgument 返回类型
     * @return CompletableFuture
     */
    abstract <T> CompletableFuture<T> getAsync(String url, Class<T> typeArgument);

    /**
     * get请求
     *
     * @param url           请求地址
     * @param headMap       请求头
     * @param paramMap      请求参数
     * @param interceptor   拦截器
     * @param httpClient    http客户端
     * @param multipartData 文件上传
     * @param returnType    返回类型
     * @return CompletableFuture
     * @throws IOException          异常
     * @throws InterruptedException 异常
     */
    abstract <T> T getSync(String url,
                           Map<String, String> headMap,
                           Map<String, String> paramMap,
                           Class<T> returnType,
                           HttpClientInterceptor interceptor,
                           HttpClient httpClient,
                           MultipartData multipartData) throws IOException, InterruptedException;

    /**
     * get请求
     *
     * @param url         请求地址
     * @param headMap     请求头
     * @param paramMap    请求参数
     * @param interceptor 拦截器
     * @param httpClient  http客户端
     * @param returnType  返回类型
     * @return CompletableFuture
     * @throws IOException          异常
     * @throws InterruptedException 异常
     */
    abstract <T> T getSync(String url,
                           Map<String, String> headMap,
                           Map<String, String> paramMap,
                           Class<T> returnType,
                           HttpClientInterceptor interceptor,
                           HttpClient httpClient) throws IOException, InterruptedException;

    /**
     * get请求
     *
     * @param url        请求地址
     * @param headMap    请求头
     * @param paramMap   请求参数
     * @param httpClient http客户端
     * @param returnType 返回类型
     * @return CompletableFuture
     * @throws IOException          异常
     * @throws InterruptedException 异常
     */
    abstract <T> T getSync(String url,
                           Map<String, String> headMap,
                           Map<String, String> paramMap,
                           Class<T> returnType,
                           HttpClient httpClient) throws IOException, InterruptedException;

    /**
     * get请求
     *
     * @param url        请求地址
     * @param returnType 返回类型
     * @return CompletableFuture
     * @throws IOException          异常
     * @throws InterruptedException 异常
     */
    abstract <T> T getSync(String url, Class<T> returnType) throws IOException, InterruptedException;

    //--------------------delete------------------------

    /**
     * delete请求
     *
     * @param url          请求地址
     * @param headMap      请求头
     * @param paramMap     请求参数
     * @param typeArgument 返回类型
     * @param interceptor  拦截器
     * @param httpClient   http客户端
     * @return CompletableFuture
     */
    abstract <T> CompletableFuture<T> deleteAsync(String url,
                                                  Map<String, String> headMap,
                                                  Map<String, String> paramMap,
                                                  Class<T> typeArgument,
                                                  HttpClientInterceptor interceptor,
                                                  HttpClient httpClient);

    /**
     * delete请求
     *
     * @param url          请求地址
     * @param headMap      请求头
     * @param paramMap     请求参数
     * @param typeArgument 返回类型
     * @param httpClient   http客户端
     * @return CompletableFuture
     */
    abstract <T> CompletableFuture<T> deleteAsync(String url,
                                                  Map<String, String> headMap,
                                                  Map<String, String> paramMap,
                                                  Class<T> typeArgument,
                                                  HttpClient httpClient);

    /**
     * delete请求
     *
     * @param url          请求地址
     * @param typeArgument 返回类型
     * @return CompletableFuture
     */
    abstract <T> CompletableFuture<T> deleteAsync(String url, Class<T> typeArgument);

    /**
     * delete请求
     *
     * @param url         请求地址
     * @param headMap     请求头
     * @param paramMap    请求参数
     * @param interceptor 拦截器
     * @param httpClient  http客户端
     * @param returnType  返回类型
     * @return CompletableFuture
     * @throws IOException          异常
     * @throws InterruptedException 异常
     */
    abstract <T> T deleteSync(String url,
                              Map<String, String> headMap,
                              Map<String, String> paramMap,
                              Class<T> returnType,
                              HttpClientInterceptor interceptor,
                              HttpClient httpClient) throws IOException, InterruptedException;

    /**
     * delete请求
     *
     * @param url        请求地址
     * @param headMap    请求头
     * @param paramMap   请求参数
     * @param httpClient http客户端
     * @param returnType 返回类型
     * @return CompletableFuture
     * @throws IOException          异常
     * @throws InterruptedException 异常
     */
    abstract <T> T deleteSync(String url,
                              Map<String, String> headMap,
                              Map<String, String> paramMap,
                              Class<T> returnType,
                              HttpClient httpClient) throws IOException, InterruptedException;

    /**
     * delete请求
     *
     * @param url        请求地址
     * @param returnType 返回类型
     * @return CompletableFuture
     * @throws IOException          异常
     * @throws InterruptedException 异常
     */
    abstract <T> T deleteSync(String url, Class<T> returnType) throws IOException, InterruptedException;

    //--------------------post------------------------

    /**
     * post请求
     *
     * @param url          请求地址
     * @param headMap      请求头
     * @param bodyObject   请求参数
     * @param typeArgument 返回类型
     * @param interceptor  拦截器
     * @param httpClient   http客户端
     * @return CompletableFuture
     */
    abstract <T> CompletableFuture<T> postAsync(String url,
                                                Map<String, String> headMap,
                                                Object bodyObject,
                                                Class<T> typeArgument,
                                                HttpClientInterceptor interceptor,
                                                HttpClient httpClient);

    /**
     * post请求
     *
     * @param url          请求地址
     * @param headMap      请求头
     * @param bodyObject   请求参数
     * @param typeArgument 返回类型
     * @param httpClient   http客户端
     * @return CompletableFuture
     */
    abstract <T> CompletableFuture<T> postAsync(String url, Map<String, String> headMap, Object bodyObject, Class<T> typeArgument, HttpClient httpClient);

    /**
     * post请求
     *
     * @param url          请求地址
     * @param bodyObject   请求参数
     * @param typeArgument 返回类型
     * @return CompletableFuture
     */
    abstract <T> CompletableFuture<T> postAsync(String url, Object bodyObject, Class<T> typeArgument);

    /**
     * post请求
     *
     * @param url         请求地址
     * @param headMap     请求头
     * @param bodyObject  请求参数
     * @param interceptor 拦截器
     * @param httpClient  http客户端
     * @param returnType  返回类型
     * @return CompletableFuture
     * @throws IOException          异常
     * @throws InterruptedException 异常
     */
    abstract <T> T postSync(String url,
                            Map<String, String> headMap,
                            Object bodyObject,
                            Class<T> returnType,
                            HttpClientInterceptor interceptor,
                            HttpClient httpClient) throws IOException, InterruptedException;

    /**
     * post请求
     *
     * @param url        请求地址
     * @param headMap    请求头
     * @param bodyObject 请求参数
     * @param httpClient http客户端
     * @param returnType 返回类型
     * @return CompletableFuture
     * @throws IOException          异常
     * @throws InterruptedException 异常
     */
    abstract <T> T postSync(String url,
                            Map<String, String> headMap,
                            Object bodyObject,
                            Class<T> returnType,
                            HttpClient httpClient) throws IOException, InterruptedException;

    /**
     * post请求
     *
     * @param url        请求地址
     * @param bodyObject 请求参数
     * @param returnType 返回类型
     * @return CompletableFuture
     * @throws IOException          异常
     * @throws InterruptedException 异常
     */
    abstract <T> T postSync(String url, Object bodyObject, Class<T> returnType) throws IOException, InterruptedException;

    //--------------------put------------------------

    /**
     * put请求
     *
     * @param url          请求地址
     * @param headMap      请求头
     * @param bodyObject   请求参数
     * @param typeArgument 返回类型
     * @param interceptor  拦截器
     * @param httpClient   http客户端
     * @return CompletableFuture
     */
    abstract <T> CompletableFuture<T> putAsync(String url,
                                               Map<String, String> headMap,
                                               Object bodyObject,
                                               Class<T> typeArgument,
                                               HttpClientInterceptor interceptor,
                                               HttpClient httpClient);

    /**
     * put请求
     *
     * @param url          请求地址
     * @param headMap      请求头
     * @param bodyObject   请求参数
     * @param typeArgument 返回类型
     * @param httpClient   http客户端
     * @return CompletableFuture
     */
    abstract <T> CompletableFuture<T> putAsync(String url,
                                               Map<String, String> headMap,
                                               Object bodyObject,
                                               Class<T> typeArgument,
                                               HttpClient httpClient);

    /**
     * put请求
     *
     * @param url          请求地址
     * @param bodyObject   请求参数
     * @param typeArgument 返回类型
     * @return CompletableFuture
     */
    abstract <T> CompletableFuture<T> putAsync(String url, Object bodyObject, Class<T> typeArgument);

    /**
     * put请求
     *
     * @param url         请求地址
     * @param headMap     请求头
     * @param bodyObject  请求参数
     * @param interceptor 拦截器
     * @param httpClient  http客户端
     * @param returnType  返回类型
     * @return CompletableFuture
     * @throws IOException          异常
     * @throws InterruptedException 异常
     */
    abstract <T> T putSync(String url,
                           Map<String, String> headMap,
                           Object bodyObject,
                           Class<T> returnType,
                           HttpClientInterceptor interceptor,
                           HttpClient httpClient) throws IOException, InterruptedException;

    /**
     * put请求
     *
     * @param url        请求地址
     * @param headMap    请求头
     * @param bodyObject 请求参数
     * @param httpClient http客户端
     * @param returnType 返回类型
     * @return CompletableFuture
     * @throws IOException          异常
     * @throws InterruptedException 异常
     */
    abstract <T> T putSync(String url,
                           Map<String, String> headMap,
                           Object bodyObject,
                           Class<T> returnType,
                           HttpClient httpClient) throws IOException, InterruptedException;

    /**
     * put请求
     *
     * @param url        请求地址
     * @param bodyObject 请求参数
     * @param returnType 返回类型
     * @return CompletableFuture
     * @throws IOException          异常
     * @throws InterruptedException 异常
     */
    abstract <T> T putSync(String url, Object bodyObject, Class<T> returnType) throws IOException, InterruptedException;

    //--------------------patch------------------------

    /**
     * patch请求
     *
     * @param url          请求地址
     * @param headMap      请求头
     * @param bodyObject   请求参数
     * @param typeArgument 返回类型
     * @param interceptor  拦截器
     * @param httpClient   http客户端
     * @return CompletableFuture
     */
    abstract <T> CompletableFuture<T> patchAsync(String url,
                                                 Map<String, String> headMap,
                                                 Object bodyObject,
                                                 Class<T> typeArgument,
                                                 HttpClientInterceptor interceptor,
                                                 HttpClient httpClient);

    /**
     * patch请求
     *
     * @param url          请求地址
     * @param headMap      请求头
     * @param bodyObject   请求参数
     * @param typeArgument 返回类型
     * @param httpClient   http客户端
     * @return CompletableFuture
     */
    abstract <T> CompletableFuture<T> patchAsync(String url,
                                                 Map<String, String> headMap,
                                                 Object bodyObject,
                                                 Class<T> typeArgument,
                                                 HttpClient httpClient);

    /**
     * patch请求
     *
     * @param url          请求地址
     * @param bodyObject   请求参数
     * @param typeArgument 返回类型
     * @return CompletableFuture
     */
    abstract <T> CompletableFuture<T> patchAsync(String url, Object bodyObject, Class<T> typeArgument);

    /**
     * patch请求
     *
     * @param url         请求地址
     * @param headMap     请求头
     * @param bodyObject  请求参数
     * @param interceptor 拦截器
     * @param httpClient  http客户端
     * @param returnType  返回类型
     * @return CompletableFuture
     * @throws IOException          异常
     * @throws InterruptedException 异常
     */
    abstract <T> T patchSync(String url,
                             Map<String, String> headMap,
                             Object bodyObject,
                             Class<T> returnType,
                             HttpClientInterceptor interceptor,
                             HttpClient httpClient) throws IOException, InterruptedException;

    /**
     * patch请求
     *
     * @param url        请求地址
     * @param headMap    请求头
     * @param bodyObject 请求参数
     * @param httpClient http客户端
     * @param returnType 返回类型
     * @return CompletableFuture
     * @throws IOException          异常
     * @throws InterruptedException 异常
     */
    abstract <T> T patchSync(String url,
                             Map<String, String> headMap,
                             Object bodyObject,
                             Class<T> returnType,
                             HttpClient httpClient) throws IOException, InterruptedException;

    /**
     * patch请求
     *
     * @param url        请求地址
     * @param bodyObject 请求参数
     * @param returnType 返回类型
     * @return CompletableFuture
     * @throws IOException          异常
     * @throws InterruptedException 异常
     */
    abstract <T> T patchSync(String url, Object bodyObject, Class<T> returnType) throws IOException, InterruptedException;

    //--------------------File------------------------

    /**
     * 文件上传
     *
     * @param method        请求方法
     * @param url           请求地址
     * @param headMap       请求头
     * @param bodyObject    请求参数
     * @param typeArgument  返回类型
     * @param interceptor   拦截器
     * @param multipartData 文件参数
     * @param httpClient    http客户端
     * @return CompletableFuture
     */
    abstract <T> CompletableFuture<T> fileAsync(HttpMethod method,
                                                String url,
                                                Map<String, String> headMap,
                                                Object bodyObject,
                                                Class<T> typeArgument,
                                                HttpClientInterceptor interceptor,
                                                MultipartData multipartData,
                                                HttpClient httpClient);

    /**
     * 文件上传
     *
     * @param method        请求方法
     * @param url           请求地址
     * @param headMap       请求头
     * @param bodyObject    请求参数
     * @param typeArgument  返回类型
     * @param multipartData 文件参数
     * @param httpClient    http客户端
     * @return CompletableFuture
     */
    abstract <T> CompletableFuture<T> fileAsync(HttpMethod method,
                                                String url,
                                                Map<String, String> headMap,
                                                Object bodyObject,
                                                Class<T> typeArgument,
                                                MultipartData multipartData,
                                                HttpClient httpClient);

    /**
     * 文件上传
     *
     * @param method        请求方法
     * @param url           请求地址
     * @param headMap       请求头
     * @param bodyObject    请求参数
     * @param returnType    返回类型
     * @param interceptor   拦截器
     * @param httpClient    http客户端
     * @param multipartData 文件参数
     * @return CompletableFuture
     * @throws IOException          异常
     * @throws InterruptedException 异常
     */
    abstract <T> T fileSync(HttpMethod method,
                            String url,
                            Map<String, String> headMap,
                            Object bodyObject,
                            Class<T> returnType,
                            HttpClientInterceptor interceptor,
                            MultipartData multipartData,
                            HttpClient httpClient) throws IOException, InterruptedException;

    /**
     * 文件上传
     *
     * @param method        请求方法
     * @param url           请求地址
     * @param headMap       请求头
     * @param bodyObject    请求参数
     * @param returnType    返回类型
     * @param httpClient    http客户端
     * @param multipartData 文件参数
     * @return CompletableFuture
     * @throws IOException          异常
     * @throws InterruptedException 异常
     */
    abstract <T> T fileSync(HttpMethod method,
                            String url,
                            Map<String, String> headMap,
                            Object bodyObject,
                            Class<T> returnType,
                            MultipartData multipartData,
                            HttpClient httpClient) throws IOException, InterruptedException;

    //--------------------form------------------------

    /**
     * form表单上传
     *
     * @param method       请求方法
     * @param url          请求地址
     * @param headMap      请求头
     * @param paramMap     请求参数
     * @param bodyObject   请求参数
     * @param typeArgument 返回类型
     * @param interceptor  拦截器
     * @param httpClient   http客户端
     * @return CompletableFuture
     */
    abstract <T> CompletableFuture<T> formAsync(HttpMethod method,
                                                String url,
                                                Map<String, String> headMap,
                                                Map<String, String> paramMap,
                                                Object bodyObject,
                                                Class<T> typeArgument,
                                                HttpClientInterceptor interceptor,
                                                HttpClient httpClient);

    /**
     * form表单上传
     *
     * @param method       请求方法
     * @param url          请求地址
     * @param headMap      请求头
     * @param paramMap     请求参数
     * @param bodyObject   请求参数
     * @param typeArgument 返回类型
     * @param httpClient   http客户端
     * @return CompletableFuture
     */
    abstract <T> CompletableFuture<T> formAsync(HttpMethod method,
                                                String url,
                                                Map<String, String> headMap,
                                                Map<String, String> paramMap,
                                                Object bodyObject,
                                                Class<T> typeArgument,
                                                HttpClient httpClient);

    /**
     * form表单上传
     *
     * @param method      请求方法
     * @param url         请求地址
     * @param headMap     请求头
     * @param paramMap    请求参数
     * @param bodyObject  请求参数
     * @param returnType  返回类型
     * @param interceptor 拦截器
     * @param httpClient  http客户端
     * @return CompletableFuture
     * @throws IOException          异常
     * @throws InterruptedException 异常
     */
    abstract <T> T formSync(HttpMethod method,
                            String url,
                            Map<String, String> headMap,
                            Map<String, String> paramMap,
                            Object bodyObject,
                            Class<T> returnType,
                            HttpClientInterceptor interceptor,
                            HttpClient httpClient) throws IOException, InterruptedException;

    /**
     * form表单上传
     *
     * @param method     请求方法
     * @param url        请求地址
     * @param headMap    请求头
     * @param paramMap   请求参数
     * @param bodyObject 请求参数
     * @param returnType 返回类型
     * @param httpClient http客户端
     * @return CompletableFuture
     * @throws IOException          异常
     * @throws InterruptedException 异常
     */
    abstract <T> T formSync(HttpMethod method,
                            String url,
                            Map<String, String> headMap,
                            Map<String, String> paramMap,
                            Object bodyObject,
                            Class<T> returnType,
                            HttpClient httpClient) throws IOException, InterruptedException;

}
