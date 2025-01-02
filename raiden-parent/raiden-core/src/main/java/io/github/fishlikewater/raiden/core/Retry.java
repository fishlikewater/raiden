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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * {@code RetryUtils}
 * 重试工具类
 *
 * @author zhangxiang
 * @version 1.0.5
 * @since 2024/09/06
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Retry<T> {

    private int retryTimes;

    private int retryInterval;

    private TimeUnit intervalTimeUnit;

    public void retry(Runnable runnable) {
        this.retry(runnable, null);
    }

    public void retry(Runnable runnable, Consumer<Exception> finalExceptionHandler) {
        for (int i = 1; i <= this.retryTimes + 1; i++) {
            try {
                runnable.run();
            } catch (Exception e) {
                if (i > this.retryTimes) {
                    if (Objects.nonNull(finalExceptionHandler)) {
                        finalExceptionHandler.accept(e);
                    } else {
                        RaidenExceptionCheck.INSTANCE.throwUnchecked(e);
                    }
                }
            }
            if (i <= this.retryTimes) {
                this.sleep();
            }
        }
    }

    public T retry(Supplier<T> supplier) {
        return this.retry(supplier, null);
    }

    public T retry(Supplier<T> supplier, Predicate<T> resultPredicate) {
        return this.retry(supplier, resultPredicate, null);
    }

    public T retry(Supplier<T> supplier, Predicate<T> resultPredicate, Function<Exception, T> finalExceptionHandler) {
        T t = null;
        for (int i = 1; i <= this.retryTimes + 1; i++) {
            try {
                t = supplier.get();
                if (Objects.isNull(resultPredicate)) {
                    return t;
                }
                if (resultPredicate.test(t)) {
                    return t;
                } else if (i > this.retryTimes) {
                    return RaidenExceptionCheck.INSTANCE.throwUnchecked("The return value is not the expected value");
                }

            } catch (Exception e) {
                if (i > this.retryTimes) {
                    if (Objects.nonNull(finalExceptionHandler)) {
                        finalExceptionHandler.apply(e);
                    } else {
                        return RaidenExceptionCheck.INSTANCE.throwUnchecked(e);
                    }
                }
            }
            if (i <= this.retryTimes) {
                this.sleep();
            }
        }
        return t;
    }

    private void sleep() {
        try {
            this.intervalTimeUnit.sleep(this.retryInterval);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
