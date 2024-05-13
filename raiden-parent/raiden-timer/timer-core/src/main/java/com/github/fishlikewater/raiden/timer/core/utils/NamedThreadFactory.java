/*
 * Copyright © 2024 zhangxiang (fishlikewater@126.com)
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
package com.github.fishlikewater.raiden.timer.core.utils;

import lombok.NonNull;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.LongAdder;

/**
 * {@code NamedThreadFactory}
 * 定时器实现
 *
 * @author zhangxiang
 * @since 2024/04/03
 * @version 1.0.0
 */
public class NamedThreadFactory implements ThreadFactory {

    private final String prefix;
    private final LongAdder threadNumber = new LongAdder();

    public NamedThreadFactory(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public Thread newThread(@NonNull Runnable runnable) {
        threadNumber.add(1);
        return new Thread(runnable, STR."\{prefix} thread-\{threadNumber.intValue()}");
    }
}