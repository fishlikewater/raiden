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
import io.github.fishlikewater.raiden.http.core.Response;

import java.io.IOException;

/**
 * {@code Interceptor}
 * 拦截器
 *
 * @author zhangxiang
 * @version 1.1.0
 * @since 2024/12/09
 */
public interface HttpInterceptor {

    Response intercept(Chain chain) throws IOException, InterruptedException;

    int order();

    interface Chain {

        RequestWrap requestWrap();

        Response proceed(RequestWrap requestWrap) throws IOException, InterruptedException;

        Response proceed() throws IOException, InterruptedException;

        void reset();
    }
}
