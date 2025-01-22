/*
 * Copyright (c) 2025 zhangxiang (fishlikewater@126.com)
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
package io.github.fishlikewater.raiden.core.handler;

import io.github.fishlikewater.raiden.core.constant.CommonConstants;
import lombok.extern.slf4j.Slf4j;

import java.io.Closeable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

/**
 * {@code PipelineContext}
 *
 * @author zhangxiang
 * @version 1.1.2
 * @since 2025/1/17
 **/
@Slf4j
public class PipelineContext implements Closeable {

    private final Map<String, Object> context = new HashMap<>(8);

    private final AtomicInteger stopped = new AtomicInteger(0);

    private PipelineHandler<?> currentHandler;

    public static PipelineContext getInstance() {
        return new PipelineContext();
    }

    private PipelineContext() {
    }

    public PipelineContext addProperty(String key, Object value) {
        context.put(key, value);
        return this;
    }

    public Object getProperty(String key) {
        return context.get(key);
    }

    public <T> T getProperty(String key, Function<Object, T> function) {
        return function.apply(context.get(key));
    }

    public void clear() {
        context.clear();
    }

    public void removeProperty(String key) {
        context.remove(key);
    }

    public void stop() {
        if (this.stopped.compareAndSet(0, 1)) {
            String currentHandleName = Objects.isNull(this.currentHandler) ? "" : this.currentHandler.getClass().getName();
            log.warn("{}.trigger.stop", currentHandleName);
        }
    }

    @Override
    public void close() {
        this.clear();
    }

    /**
     * 判断是否终止
     *
     * @return 是否终止
     */
    protected boolean isStopped() {
        return this.stopped.get() == CommonConstants.INT_ONE;
    }

    protected void setCurrentHandler(PipelineHandler<?> handler) {
        this.currentHandler = handler;
    }
}
