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
package io.github.fishlikewater.raiden.http.core.convert;

import io.github.fishlikewater.raiden.http.core.exception.HttpExceptionCheck;
import io.github.fishlikewater.raiden.http.core.uttils.ByteBufferUtils;
import io.github.fishlikewater.raiden.json.core.JSONUtils;
import lombok.extern.slf4j.Slf4j;

import java.net.http.HttpHeaders;
import java.net.http.HttpResponse;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Flow;

/**
 * <p>
 *
 * </p>
 *
 * @author fishlikewater@126.com
 * @version 1.0.0
 * @since 2023年09月23日 10:14
 **/
@Slf4j
public class ResponseJsonHandlerSubscriber<T> implements HttpResponse.BodySubscriber<T> {

    Flow.Subscription subscription;

    private final CompletableFuture<T> result = new CompletableFuture<>();

    private final HttpHeaders headers;

    private final Class<?> clazz;

    private final List<ByteBuffer> received = new ArrayList<>();

    public ResponseJsonHandlerSubscriber(HttpHeaders headers, Class<?> clazz) {
        this.headers = headers;
        this.clazz = clazz;
    }

    @Override
    public CompletionStage<T> getBody() {
        return result;
    }

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
    public void onNext(List<ByteBuffer> items) {
        assert ByteBufferUtils.hasRemaining(items);
        received.addAll(items);
    }

    @Override
    public void onError(Throwable throwable) {
        received.clear();
        result.completeExceptionally(throwable);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onComplete() {
        final byte[] bytes = ByteBufferUtils.join(received);
        final Charset charset = ByteBufferUtils.charsetFrom(headers);
        final String jsonStr = new String(bytes, charset);
        Object bean;
        if (clazz.isAssignableFrom(String.class) || clazz.isAssignableFrom(Number.class)) {
            bean = jsonStr;
            result.complete((T) bean);
            return;
        }
        try {
            bean = JSONUtils.JACKSON.readValue(jsonStr, clazz);
            result.complete((T) bean);
        } catch (Exception e) {
            HttpExceptionCheck.INSTANCE.throwUnchecked(e);
        }
    }
}
