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
package io.github.fishlikewater.raiden.core.future;

/**
 * {@code FutureListener}
 * 监听器
 *
 * @author zhangxiang
 * @since 2025/8/5
 */
@FunctionalInterface
public interface FutureListener<V> {

    /**
     * 当 Future 完成时调用。
     *
     * @param future 完成的 Future
     * @throws Exception 如果监听器处理过程中发生错误
     */
    void operationComplete(RobustFuture<V> future) throws Exception;
}
