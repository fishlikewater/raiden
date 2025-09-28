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
package io.github.fishlikewater.raiden.core;

/**
 * {@code PreAction}
 * 前置和后置执行
 *
 * @author zhangxiang
 * @since 2025/9/28
 */
public interface PreAction {

    /**
     * 前置处理
     */
    void preAction();

    /**
     * 后置处理
     */
    void postAction();

    /**
     * 验证处理
     */
    void validateAction();

    /**
     * 组合处理
     */
    default void compositeAction() {
        this.validateAction();
        this.preAction();
    }
}
