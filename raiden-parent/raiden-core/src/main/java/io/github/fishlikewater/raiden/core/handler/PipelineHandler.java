/*
 * Copyright (c) 2023-2025 zhangxiang (fishlikewater@126.com)
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

/**
 * {@code PipelineHandler}
 *
 * @author fishlikewater@126.com
 * @version 1.0.5
 * @since 2024年08月22日 22:15
 **/
@Slf4j
public abstract class PipelineHandler<T> {

    private PipelineHandler<T> chain;

    /**
     * 执行
     *
     * @param t 待处理数据
     */
    public abstract void doHandle(T t, PipelineContext context);

    public final void handle(T t, PipelineContext context) {
        context.setCurrentHandler(this);
        this.doHandle(t, context);
        if (this.chain != null && !context.isStopped()) {
            this.chain.handle(t, context);
        }
        if (context.getPipeline().isReuse()) {
            return;
        }
        this.close();
    }

    public void close() {
        this.chain = null;
    }

    protected void next(PipelineHandler<T> handler) {
        this.chain = handler;
    }
}
