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
package io.github.fishlikewater;

import io.github.fishlikewater.raiden.core.future.RobustFuture;
import org.junit.Test;

import java.util.concurrent.ExecutionException;

/**
 * {@code RobustFutureTest}
 *
 * @author zhangxiang
 * @since 2025/8/6
 */
public class RobustFutureTest {

    @Test
    public void test() throws InterruptedException, ExecutionException {
        RobustFuture<String> future = new RobustFuture<>();
        future.addListener(f -> {
            if (f.isSuccess()) {
                System.out.println(f.getNow());
            }
            if (!f.isSuccess()) {
                System.out.println(f.cause().getMessage());
            }
        });
        RobustFuture<String> thenApply = future.thenApply(String::toUpperCase);
        future.setSuccess("hello world");
        assert "HELLO WORLD".equals(thenApply.get());

        RobustFuture<Void> future2 = new RobustFuture<>();
        RobustFuture<String> stringRobustFuture = future2.thenCompose(s -> {
            RobustFuture<String> robustFuture = new RobustFuture<>();
            robustFuture.addListener(f -> {
                if (f.isSuccess()) {
                    System.out.println(f.getNow());
                }
            });
            robustFuture.setSuccess("java");
            return robustFuture;
        });
        future2.complete();
        String string1 = stringRobustFuture.get();
        assert "java".equals(string1);
    }
}
