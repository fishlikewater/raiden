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
package io.github.fishlikewater.raiden.core;

import io.github.fishlikewater.raiden.core.exception.RaidenExceptionCheck;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.Executor;

/**
 * {@code Composite}
 * 常用的方法
 *
 * @author zhangxiang
 * @version 1.0.2
 * @since 2024/06/14
 */
public interface Composite {

    /**
     * 并行执行任务
     *
     * @param tasks    任务列表
     * @param executor 执行器
     */
    default void parallel(List<Runnable> tasks, Executor executor) {
        if (tasks == null || tasks.isEmpty()) {
            return;
        }

        if (executor == null) {
            RaidenExceptionCheck.INSTANCE.throwUnchecked("Executor is not properly initialized.");
        }
        List<CompletableFuture<?>> futures = LambdaUtils.toList(
                tasks,
                task -> CompletableFuture.runAsync(task, executor));
        try {
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        } catch (CompletionException e) {
            RaidenExceptionCheck.INSTANCE.throwUnchecked("One of the parallel tasks failed", e);
        }
    }
}
