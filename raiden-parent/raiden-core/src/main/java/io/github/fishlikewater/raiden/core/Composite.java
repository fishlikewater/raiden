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
package io.github.fishlikewater.raiden.core;

import io.github.fishlikewater.raiden.core.exception.RaidenExceptionCheck;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Supplier;

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
        List<CompletableFuture<?>> futures = this.getCompletableFutures(tasks, executor);
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
    }

    /**
     * 并行执行任务(任意一个执行成功)
     *
     * @param tasks    任务列表
     * @param executor 执行器
     */
    default void parallelAny(List<Runnable> tasks, Executor executor) {
        List<CompletableFuture<?>> futures = this.getCompletableFutures(tasks, executor);
        CompletableFuture.anyOf(futures.toArray(new CompletableFuture[0])).join();
    }

    /**
     * 并行执行任务(任意一个执行成功)
     *
     * @param tasks    任务列表
     * @param executor 执行器
     * @return 任务结果列表
     */
    @SuppressWarnings("unchecked")
    default <T> T parallelAnyCallable(List<Supplier<T>> tasks, Executor executor) {
        List<CompletableFuture<T>> futures = this.getCompletableFuturesCallable(tasks, executor);
        return (T) CompletableFuture.anyOf(futures.toArray(new CompletableFuture[0])).join();
    }

    /**
     * 并行执行任务
     *
     * @param tasks    任务列表
     * @param executor 执行器\
     * @return 任务结果列表
     */
    default List<CompletableFuture<?>> getCompletableFutures(List<Runnable> tasks, Executor executor) {
        if (ObjectUtils.isNullOrEmpty(tasks)) {
            return null;
        }

        if (executor == null) {
            RaidenExceptionCheck.INSTANCE.throwUnchecked("Executor.is.not.properly.initialized.");
        }
        return LambdaUtils.toList(
                tasks,
                task -> CompletableFuture.runAsync(task, executor));
    }

    /**
     * 并行执行任务
     *
     * @param tasks    任务列表
     * @param executor 执行器\
     * @return 任务结果列表
     */
    default <T> List<CompletableFuture<T>> getCompletableFuturesCallable(List<Supplier<T>> tasks, Executor executor) {
        if (ObjectUtils.isNullOrEmpty(tasks)) {
            return null;
        }

        if (executor == null) {
            RaidenExceptionCheck.INSTANCE.throwUnchecked("Executor.is.not.properly.initialized.");
        }
        return LambdaUtils.toList(
                tasks,
                task -> CompletableFuture.supplyAsync(task, executor));
    }
}
