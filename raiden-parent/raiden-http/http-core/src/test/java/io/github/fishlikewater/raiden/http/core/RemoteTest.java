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
package io.github.fishlikewater.raiden.http.core;

import io.github.fishlikewater.raiden.http.core.remote.DemoRemote;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.net.http.HttpClient;
import java.util.concurrent.CompletableFuture;

/**
 * {@code RemoteTest}
 *
 * @author fishlikewater@126.com
 * @since 2024/03/20
 * @version 1.0.0
 */
public class RemoteTest {

    @Before
    public void before() throws ClassNotFoundException {
        HttpBootStrap.setSelfManager(true);
        HttpBootStrap.init("io.github.fishlikewater.raiden.http.core.remote");
        HttpBootStrap.registerHttpClient("third", HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).build());
        HttpBootStrap.getLogConfig().setEnableLog(false).setLogLevel(LogConfig.LogLevel.BASIC);
    }

    @Test
    public void test() {
        DemoRemote remote = HttpBootStrap.getProxy(DemoRemote.class);
        String s = remote.baidu();
        Assert.assertNotNull(s);
    }

    @Test
    public void testAsync() throws InterruptedException {
        DemoRemote remote = HttpBootStrap.getProxy(DemoRemote.class);
        CompletableFuture<String> future = remote.baidu5();
        future.thenAcceptAsync(System.out::println);
        System.out.println("11");
        Thread.sleep(30_000);
    }
}
