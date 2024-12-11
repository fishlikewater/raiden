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

import io.github.fishlikewater.raiden.core.ObjectUtils;
import io.github.fishlikewater.raiden.http.core.RequestWrap;
import io.github.fishlikewater.raiden.http.core.Response;

import java.io.IOException;
import java.util.List;

/**
 * {@code RealInterceptorChain}
 *
 * @author zhangxiang
 * @version 1.1.0
 * @since 2024/12/09
 */
public class RealInterceptorChain implements HttpInterceptor.Chain {

    private final List<HttpInterceptor> interceptors;
    private RequestWrap requestWrap;
    private int index;

    public RealInterceptorChain(RequestWrap requestWrap, List<HttpInterceptor> interceptors) {
        this.requestWrap = requestWrap;
        this.interceptors = interceptors;
    }

    @Override
    public RequestWrap requestWrap() {
        return this.requestWrap;
    }

    @Override
    public Response proceed(RequestWrap requestWrap) throws IOException, InterruptedException {
        if (this.index >= this.interceptors.size()) {
            throw new AssertionError();
        }
        HttpInterceptor interceptor = this.interceptors.get(index);
        this.incrementIndex();
        this.updateRequestWrap(requestWrap);
        Response response = interceptor.intercept(this);
        if (ObjectUtils.isNullOrEmpty(response)) {
            throw new NullPointerException("interceptor " + interceptor + " returned null");
        }
        return response;
    }

    @Override
    public Response proceed() throws IOException, InterruptedException {
        return this.proceed(this.requestWrap);
    }

    @Override
    public void reset() {
        this.index = 0;
    }

    private void updateRequestWrap(RequestWrap requestWrap) {
        this.requestWrap = requestWrap;
    }

    private void incrementIndex() {
        this.index++;
    }
}
