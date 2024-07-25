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

import cn.hutool.core.thread.NamedThreadFactory;
import io.github.fishlikewater.raiden.core.Composite;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * {@code CompositeTest}
 *
 * @author zhangxiang
 * @version 1.0.5
 * @since 2024/07/25
 */
public class CompositeTest implements Composite {

    private ThreadPoolExecutor threadPoolExecutor;

    @Before
    public void buildThreadPoolExecutor() {
        threadPoolExecutor = new ThreadPoolExecutor(
                5,
                10,
                0,
                TimeUnit.MINUTES,
                new LinkedBlockingQueue<>(),
                new NamedThreadFactory("test", true));
    }

    @Test
    public void testParallel() {
        List<Runnable> list = new ArrayList<>();
        list.add(() -> {
            System.out.println("1");
        });
        list.add(() -> {
            System.out.println("2");
        });
        this.parallel(list, threadPoolExecutor);
    }

    @Test
    public void testParallelAny() {
        List<Runnable> list = new ArrayList<>();
        list.add(() -> {
            System.out.println("1");
        });
        list.add(() -> {
            System.out.println("2");
        });
        this.parallelAny(list, threadPoolExecutor);
    }

    @Test
    public void testParallelAnyCallable() {
        List<Supplier<String>> list = new ArrayList<>();
        list.add(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "1";
        });
        list.add(() -> {
            return "2";
        });
        list.add(() -> {
            return "3";
        });
        String string = this.parallelAnyCallable(list, threadPoolExecutor);
        System.out.println(string);
    }

    @Test
    public void test() {
        for (int i = 0; i < 3; i++) {
            this.testParallelAnyCallable();
        }
    }

    @After
    public void shutdown() {
        threadPoolExecutor.close();
    }
}
