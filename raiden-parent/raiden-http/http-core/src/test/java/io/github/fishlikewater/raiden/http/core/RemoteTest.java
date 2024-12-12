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

import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import io.github.fishlikewater.raiden.http.core.degrade.DemoGlobalBreakerConfigRegister;
import io.github.fishlikewater.raiden.http.core.degrade.sentinel.SentinelDegradeRule;
import io.github.fishlikewater.raiden.http.core.enums.DegradeType;
import io.github.fishlikewater.raiden.http.core.enums.LogLevel;
import io.github.fishlikewater.raiden.http.core.exception.DegradeException;
import io.github.fishlikewater.raiden.http.core.remote.DemoFile;
import io.github.fishlikewater.raiden.http.core.remote.DemoRemote;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.time.Duration;
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
                .setMaxRetryCount(0)
                .setRetryInterval(2000);

        CircuitBreakerConfig config = CircuitBreakerConfig.custom()
                .failureRateThreshold(50) // 当失败率达到50%时打开断路器
                .waitDurationInOpenState(Duration.ofSeconds(10)) // 断路器打开状态持续时间
                .slidingWindowSize(10) // 滑动窗口大小
                .minimumNumberOfCalls(3) // 最少调用次数以激活断路器逻辑
                .build();

        SentinelDegradeRule rule = new SentinelDegradeRule();
        rule.setGrade(RuleConstant.DEGRADE_GRADE_RT);
        rule.setCount(10);
        rule.setMinRequestAmount(3);
        rule.setTimeWindow(2);
        rule.setSlowRatioThreshold(1.0);

        HttpBootStrap.registerGlobalBreakerConfig(new DemoGlobalBreakerConfigRegister());

        HttpBootStrap.getConfig()
                .getBreakerConfigRegistry()
                .register("test", config);

        HttpBootStrap.getConfig()
                .getSentDegradeRuleRegistry()
                .register("test", rule);

        HttpBootStrap.getConfig()
                .setEnableDegrade(false)
                .setDegradeType(DegradeType.RESILIENCE4J);
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
        Thread.sleep(3_000);
    }

    @Test
    public void testUpload() throws FileNotFoundException, URISyntaxException {
        DemoFile remote = HttpBootStrap.getProxy(DemoFile.class);
        URL resource = getClass().getClassLoader().getResource("1.png");
        if (resource != null) {
            File file = new File(resource.toURI());
            MultipartData multipartData = MultipartData.ofFileUpload(new FileInputStream(file), "test", file.length());
            String s = remote.uploadFile(multipartData);
            System.out.println(s);
        }
    }

    @Test
    public void testUpload2() throws URISyntaxException {
        DemoFile remote = HttpBootStrap.getProxy(DemoFile.class);
        URL resource = getClass().getClassLoader().getResource("1.png");
        if (resource != null) {
            File file = new File(resource.toURI());
            MultipartData multipartData = MultipartData.ofFileUpload(file);
            String s = remote.uploadFile(multipartData);
            System.out.println(s);
        }
    }

    @Test
    public void testUpload3() throws URISyntaxException {
        DemoFile remote = HttpBootStrap.getProxy(DemoFile.class);
        URL resource1 = getClass().getClassLoader().getResource("1.png");
        URL resource2 = getClass().getClassLoader().getResource("2.png");
        if (resource1 != null && resource2 != null) {
            File file1 = new File(resource1.toURI());
            File file2 = new File(resource2.toURI());
            MultipartData multipartData = MultipartData.ofFileUpload(file1, file2);
            String s = remote.uploadFile(multipartData);
            System.out.println(s);
        }
    }

    @Test
    public void testDownload() throws Exception {
        DemoFile remote = HttpBootStrap.getProxy(DemoFile.class);
        InputStream inputStream = remote.download();
        OutputStream outputStream = new FileOutputStream("output.png");
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        System.out.println("File saved successfully.");
        outputStream.flush();
        outputStream.close();
        inputStream.close();

    }

    @Test
    public void testDownloadAsync() throws Exception {
        DemoFile remote = HttpBootStrap.getProxy(DemoFile.class);
        CompletableFuture<InputStream> completableFuture = remote.downloadAsync();
        InputStream inputStream = completableFuture.get();
        OutputStream outputStream = new FileOutputStream("output.png");
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        System.out.println("File saved successfully.");
        outputStream.flush();
        outputStream.close();
        inputStream.close();
    }

    @Test
    public void testDegrade() throws InterruptedException {
        DemoRemote remote = HttpBootStrap.getProxy(DemoRemote.class);
        for (int i = 0; i < 30; i++) {
            try {
                String s = remote.baidu();
                System.out.println(s);
            } catch (DegradeException e) {
                System.out.println("熔断了");
            } catch (Exception e) {
                System.out.println(e.getCause().getMessage());
            }
        }
    }
}
