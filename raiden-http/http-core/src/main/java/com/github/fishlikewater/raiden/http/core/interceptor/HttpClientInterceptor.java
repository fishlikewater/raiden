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
package com.github.fishlikewater.raiden.http.core.interceptor;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * <p>
 *  请求拦截器
 * </p>
 *
 * @author fishlikewater@126.com
 * @since 2023年09月22日 19:07
 * @version 1.0.0
 **/
public interface HttpClientInterceptor {

    /**
     * 发送请求之前
     *
     * @param httpRequest 请求数据
     * @return {@code HttpRequest}
     */
    HttpRequest requestBefore(HttpRequest httpRequest);

    /**
     * 发送请求之后
     *
     * @param response 响应
     * @return {@code HttpResponse}
     */
    <T> HttpResponse<T> requestAfter(HttpResponse<T> response);
}
