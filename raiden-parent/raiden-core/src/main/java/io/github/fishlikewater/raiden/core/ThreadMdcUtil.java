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

import org.slf4j.MDC;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;

/**
 * <p>
 * 包装requestId 传递到子线程
 * </p>
 *
 * @author zhangxiang
 * @version 1.1.7
 * @since 2025/07/17 19:16
 **/
public class ThreadMdcUtil {

    private static final String TRACE_ID = "traceId";

    /**
     * 获取唯一性标识
     */
    public static String generateTraceId() {
        return UUID.randomUUID().toString().replaceAll("-", "").toLowerCase();
    }

    public static void setTraceId() {
        if (ObjectUtils.isNotNullOrEmpty(MDC.get(TRACE_ID))) {
            MDC.put(TRACE_ID, generateTraceId());
        }
    }

    /**
     * 用于父线程向线程池中提交任务时，将自身MDC中的数据复制给子线程
     *
     * @param callable 线程池中执行的任务
     * @param context  MDC上下文
     * @return Callable
     */
    public static <T> Callable<T> wrap(final Callable<T> callable, final Map<String, String> context) {
        return () -> {
            if (context == null) {
                MDC.clear();
            } else {
                MDC.setContextMap(context);
            }
            setTraceId();
            try {
                return callable.call();
            } finally {
                MDC.clear();
            }
        };
    }

    /**
     * 用于父线程向线程池中提交任务时，将自身MDC中的数据复制给子线程
     *
     * @param runnable 线程池中执行的任务
     * @param context  MDC上下文
     * @return Callable
     */
    public static Runnable wrap(final Runnable runnable, final Map<String, String> context) {
        return () -> {
            if (context == null) {
                MDC.clear();
            } else {
                MDC.setContextMap(context);
            }
            setTraceId();
            try {
                runnable.run();
            } finally {
                MDC.clear();
            }
        };
    }
}
