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

import lombok.Getter;
import lombok.Setter;

import java.util.function.Function;

/**
 * {@code Pipeline}
 *
 * @author zhangxiang
 * @version 1.1.2
 * @since 2025/1/17
 **/
public class Pipeline<T> {

    private PipelineHandler<T> head;

    private PipelineHandler<T> tail;

    /**
     * 是否复用
     */
    @Setter
    @Getter
    private boolean reuse = false;

    public Pipeline<T> addHandler(PipelineHandler<T> handler) {
        if (this.head == null) {
            this.head = this.tail = handler;
            return this;
        }
        this.tail.next(handler);
        this.tail = handler;

        return this;
    }

    public Pipeline<T> addFirstHandler(PipelineHandler<T> handler) {
        if (this.head == null) {
            this.head = this.tail = handler;
            return this;
        }
        handler.next(this.head);
        this.head = handler;

        return this;
    }

    public Pipeline<T> addLastHandler(PipelineHandler<T> handler) {
        return this.addHandler(handler);
    }

    public void start(T t) {
        try (PipelineContext context = PipelineContext.getInstance()) {
            context.setPipeline(this);
            if (this.head != null) {
                this.head.handle(t, context);
            }
        }
    }

    public <K> K start(T t, Function<PipelineContext, K> function) {
        try (PipelineContext context = PipelineContext.getInstance()) {
            context.setPipeline(this);
            if (this.head != null) {
                this.head.handle(t, context);
            }
            return function.apply(context);
        }
    }
}
