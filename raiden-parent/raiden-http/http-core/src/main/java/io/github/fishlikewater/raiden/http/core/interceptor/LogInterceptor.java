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


import io.github.fishlikewater.raiden.http.core.HttpBootStrap;
import io.github.fishlikewater.raiden.http.core.RequestWrap;
import io.github.fishlikewater.raiden.http.core.enums.LogLevel;
import io.github.fishlikewater.raiden.http.core.uttils.ByteBufferUtils;
import lombok.extern.slf4j.Slf4j;

import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Flow;

/**
 * 日志拦截器配置
 *
 * @author fishlikewater@126.com
 * @version 1.0.0
 * @since 2023年09月25日 15:00
 **/
@Slf4j
public class LogInterceptor implements HttpClientInterceptor {

    private static final String MULTIPART_CONTENT_TYPE = "multipart/form-data";

    @Override
    public void requestBefore(RequestWrap requestWrap) {
        HttpRequest httpRequest = requestWrap.getHttpRequest();
        final LogLevel logLevel = HttpBootStrap.getConfig().getLogLevel();
        final HttpHeaders headers = httpRequest.headers();
        log.info("请求地址: {}", httpRequest.uri().toString());
        log.info("请求方法: {}", httpRequest.method());
        recordHeads(logLevel, headers);
        if (logLevel == LogLevel.DETAIL) {
            httpRequest.bodyPublisher().ifPresent(bodyPublisher -> {
                final Optional<String> contentType = headers.firstValue("Content-Type");
                contentType.ifPresent(s -> {
                    if (!s.contains(MULTIPART_CONTENT_TYPE)) {
                        getRequestData(bodyPublisher);
                    }
                });
            });
        }
    }

    @Override
    public <T> HttpResponse<T> requestAfter(RequestWrap requestWrap, HttpResponse<T> response) {
        log.info("----------------------------------------------------------------");
        log.info("响应信息: ");
        final LogLevel logLevel = HttpBootStrap.getConfig().getLogLevel();
        final int state = response.statusCode();
        log.info("{}<-{}", state, response.uri().toString());
        final HttpHeaders headers = response.headers();
        recordHeads(logLevel, headers);
        if (logLevel == LogLevel.DETAIL) {
            final String responseStr = response.body().toString();
            log.info("响应数据: {}", responseStr);
        }
        log.info("----------------------------------------------------------------");
        return response;
    }

    private void recordHeads(LogLevel logLevel, HttpHeaders headers) {
        if (logLevel == LogLevel.HEADS || logLevel == LogLevel.DETAIL) {
            final Map<String, List<String>> map = headers.map();
            map.forEach((k, v) -> log.info("{}: {}", k, v));
        }
    }

    private static void getRequestData(HttpRequest.BodyPublisher bodyPublisher) {
        bodyPublisher.subscribe(new Flow.Subscriber<>() {

            private final List<ByteBuffer> received = new ArrayList<>();
            Flow.Subscription subscription;

            @Override
            public void onSubscribe(Flow.Subscription subscription) {
                if (this.subscription != null) {
                    subscription.cancel();
                    return;
                }
                this.subscription = subscription;
                subscription.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(ByteBuffer item) {
                assert item.hasRemaining();
                received.add(item);
            }

            @Override
            public void onError(Throwable throwable) {
                log.error("", throwable);
            }

            @Override
            public void onComplete() {
                final byte[] bytes = ByteBufferUtils.join(received);
                final String jsonStr = new String(bytes, StandardCharsets.UTF_8);
                log.info("请求数据: {}", jsonStr);
            }
        });
    }
}
