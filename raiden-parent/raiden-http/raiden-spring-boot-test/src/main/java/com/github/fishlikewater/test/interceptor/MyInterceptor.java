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
package com.github.fishlikewater.test.interceptor;

import com.github.fishlikewater.raiden.http.core.interceptor.HttpClientInterceptor;
import org.springframework.stereotype.Component;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * <p>
 *
 * </p>
 *
 * @author fishlikewater@126.com
 * @since 2023年09月23日 13:41
 **/
@Component
public class MyInterceptor implements HttpClientInterceptor {

    @Override
    public HttpRequest requestBefore(HttpRequest httpRequest) {
        System.out.println("自定义拦截器--请求");
        return httpRequest;
    }

    @Override
    public <T> HttpResponse<T> requestAfter(HttpResponse<T> response) {
        System.out.println("自定义拦截器--响应");
        return response;
    }
}
