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

import io.github.fishlikewater.raiden.http.core.enums.LogLevel;
import io.github.fishlikewater.raiden.http.core.remote.DemoFile;
import io.github.fishlikewater.raiden.http.core.remote.DemoRemote;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.http.HttpClient;
import java.util.concurrent.CompletableFuture;

/**
 * {@code RemoteTest}
 *
 * @author fishlikewater@126.com
 * @version 1.0.0
 * @since 2024/03/20
 */
public class RemoteTest {

    @Before
    public void before() throws ClassNotFoundException {
        HttpBootStrap.setSelfManager(true);
        HttpBootStrap.init("io.github.fishlikewater.raiden.http.core.remote");
        HttpBootStrap.registerHttpClient("third", HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).build());
        HttpBootStrap.getConfig()
                .setEnableLog(false)
                .setLogLevel(LogLevel.BASIC)
                .setMaxRetryCount(1)
                .setRetryInterval(2000);
    }

    @Test
    public void test() {
        DemoRemote remote = HttpBootStrap.getProxy(DemoRemote.class);
        String s = remote.baidu();
        System.out.println(s);
        Assert.assertNotNull(s);
    }

    @Test
    public void testAsync() throws InterruptedException {
        DemoRemote remote = HttpBootStrap.getProxy(DemoRemote.class);
        CompletableFuture<String> future = remote.baidu5();
        future.thenAcceptAsync(System.out::println).join();
        System.out.println("11");
        Thread.sleep(3_000);
    }

    @Test
    public void testUpload() throws FileNotFoundException {
        DemoFile remote = HttpBootStrap.getProxy(DemoFile.class);
        File file = new File("C:\\Users\\fishl\\Pictures\\【哲风壁纸】剑客-红装.png");
        MultipartData multipartData = MultipartData.ofFileUpload(new FileInputStream(file), "test", file.length());
        String s = remote.uploadFile(multipartData);
        System.out.println(s);
    }

    @Test
    public void testUpload2() throws FileNotFoundException {
        DemoFile remote = HttpBootStrap.getProxy(DemoFile.class);
        File file = new File("C:\\Users\\fishl\\Pictures\\【哲风壁纸】剑客-红装.png");
        MultipartData multipartData = MultipartData.ofFileUpload(file);
        String s = remote.uploadFile(multipartData);
        System.out.println(s);
    }

    @Test
    public void testUpload3() throws FileNotFoundException {
        DemoFile remote = HttpBootStrap.getProxy(DemoFile.class);
        File file1 = new File("G:\\壁纸\\【哲风壁纸】剑客-红装.png");
        File file2 = new File("G:\\壁纸\\【哲风壁纸】美女-黑丝.png");
        MultipartData multipartData = MultipartData.ofFileUpload(file1, file2);
        String s = remote.uploadFile(multipartData);
        System.out.println(s);
    }
}
