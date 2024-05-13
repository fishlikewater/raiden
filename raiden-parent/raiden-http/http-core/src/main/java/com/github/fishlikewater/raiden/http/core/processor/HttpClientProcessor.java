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

import com.github.fishlikewater.raiden.http.core.MultipartData;
import com.github.fishlikewater.raiden.http.core.enums.HttpMethod;
import com.github.fishlikewater.raiden.http.core.interceptor.HttpClientInterceptor;

import java.lang.reflect.Type;
import java.net.http.HttpClient;
import java.util.Map;

/**
 *
 * @author fishlikewater@126.com
 * @since 2021年12月26日 18:42
 * @version 1.0.0
 **/
public interface HttpClientProcessor {

    /**
     * 处理请求
     * @author fishlikewater@126.com
     * @param method 请求方法
     * @param headMap 请求头
     * @param returnType 返回类型
     * @param typeArgument 返回类型的泛型
     * @param form 是否为form表单请求
     * @param url 请求地址
     * @param paramMap 请求路径上的参数
     * @param bodyObject 请求body数据
     * @param interceptor 拦截器
     * @param multipartData 文件数据
     * @param httpClient 客户端
     * @since 2023/9/26 14:21
     * @return java.lang.Object
     */
    Object handler(HttpMethod method, Map<String, String> headMap, Class<?> returnType, Type typeArgument, boolean form,
                   String url, Map<String, String> paramMap, Object bodyObject, HttpClientInterceptor interceptor, MultipartData multipartData, HttpClient httpClient);
}
