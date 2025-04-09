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
package io.github.fishlikewater.spring.boot.raiden.core;


import io.github.fishlikewater.raiden.core.ObjectUtils;
import io.github.fishlikewater.raiden.core.exception.RaidenExceptionCheck;
import io.github.fishlikewater.raiden.core.handler.Pipeline;
import io.github.fishlikewater.raiden.core.handler.PipelineContext;
import io.github.fishlikewater.raiden.core.handler.PipelineHandler;
import lombok.Getter;

import java.util.function.Function;

/**
 * {@code PipelineChainHelper}
 * 处理链构建器
 *
 * @author zhangxiang
 * @since 1.1.4
 */
public class PipelineChainHelper {

    public static <T> PipelineChain<T> newChain() {
        return new PipelineChain<>();
    }


    @Getter
    public static class PipelineChain<T> {

        private final Pipeline<T> pipeline;

        public PipelineChain() {
            this.pipeline = new Pipeline<>();
        }

        public PipelineChain<T> addHandler(Class<? extends PipelineHandler<T>> clazz) {
            PipelineHandler<T> handler = SpringUtils.getBean(clazz);
            if (ObjectUtils.isNullOrEmpty(handler)) {
                RaidenExceptionCheck.INSTANCE.throwUnchecked("this bean not exist!!!, className: {}", clazz.getSimpleName());
            }
            this.pipeline.addHandler(handler);
            return this;
        }

        public PipelineChain<T> addFirstHandler(Class<? extends PipelineHandler<T>> clazz) {
            PipelineHandler<T> handler = SpringUtils.getBean(clazz);
            if (ObjectUtils.isNullOrEmpty(handler)) {
                RaidenExceptionCheck.INSTANCE.throwUnchecked("this bean not exist!!!, className: {}", clazz.getSimpleName());
            }
            this.pipeline.addFirstHandler(handler);
            return this;
        }

        public PipelineChain<T> addLastHandler(Class<? extends PipelineHandler<T>> clazz) {
            PipelineHandler<T> handler = SpringUtils.getBean(clazz);
            if (ObjectUtils.isNullOrEmpty(handler)) {
                RaidenExceptionCheck.INSTANCE.throwUnchecked("this bean not exist!!!, className: {}", clazz.getSimpleName());
            }
            this.pipeline.addLastHandler(handler);
            return this;
        }

        public void start(T t) {
            this.pipeline.start(t);
        }

        public <K> K start(T t, Function<PipelineContext, K> function) {
            return this.pipeline.start(t, function);
        }

    }
}
