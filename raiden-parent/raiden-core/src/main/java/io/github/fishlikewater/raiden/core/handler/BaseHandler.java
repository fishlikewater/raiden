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

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>
 * {@code Result}
 *
 * @author fishlikewater@126.com
 * @version 1.0.5
 * @since 2024年08月22日 22:15
 **/
@Slf4j
public abstract class BaseHandler<T> {

    private BaseHandler<T> chain;

    private final AtomicInteger stopped = new AtomicInteger(0);

    /**
     * 执行
     *
     * @param t 待处理数据
     */
    public abstract void doHandle(T t, ChainContext context);

    public final void handle(T t, ChainContext context) {
        this.doHandle(t, context);
        if (this.chain != null && !isStopped()) {
            this.chain.handle(t, context);
        }
        this.close();
    }

    public void stop() {
        if (this.stopped.compareAndSet(0, 1)) {
            log.warn("handler.active.stop");
        }
    }

    public void close() {
        this.chain = null;
    }

    protected void next(BaseHandler<T> handler) {
        this.chain = handler;
    }

    private boolean isStopped() {
        return this.stopped.get() == 1;
    }
}
