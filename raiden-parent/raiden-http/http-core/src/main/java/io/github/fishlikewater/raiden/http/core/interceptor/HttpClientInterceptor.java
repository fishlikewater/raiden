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
package io.github.fishlikewater.raiden.http.core.interceptor;

import io.github.fishlikewater.raiden.http.core.RequestWrap;

import java.net.http.HttpResponse;

/**
 * <p>
 * 请求拦截器
 * </p>
 *
 * @author fishlikewater@126.com
 * @version 1.0.0
 * @since 2023年09月22日 19:07
 **/
public interface HttpClientInterceptor {

    /**
     * 发送请求之前
     *
     * @param requestWrap 请求数据
     */
    void requestBefore(RequestWrap requestWrap);

    /**
     * 发送请求之后
     *
     * @param requestWrap 请求数据
     * @param response    响应
     * @return {@code HttpResponse}
     */
    <T> HttpResponse<T> requestAfter(RequestWrap requestWrap, HttpResponse<T> response);
}
