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
package io.github.fishlikewater;

import io.github.fishlikewater.raiden.core.Retry;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * {@code RetryTest}
 *
 * @author zhangxiang
 * @version 1.0.5
 * @since 2024/09/06
 */
public class RetryTest {

    @Test
    public void testRetry() {
        Retry<Void> retry = Retry.<Void>builder()
                .intervalTimeUnit(TimeUnit.SECONDS)
                .retryInterval(1)
                .retryTimes(3)
                .build();
        retry.retry(() -> {
            System.out.println("retry");
            System.out.println(1 / 0);
        });
    }

    @Test
    public void testRetry2() {
        Retry<Integer> retry = Retry.<Integer>builder()
                .intervalTimeUnit(TimeUnit.SECONDS)
                .retryInterval(1)
                .retryTimes(3)
                .build();
        Integer integer = retry.retry(() -> 2, t -> t > 3);
        System.out.println(integer);
    }
}
