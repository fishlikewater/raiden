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

import io.github.fishlikewater.raiden.http.core.client.AbstractHttpRequestClient;
import io.github.fishlikewater.raiden.http.core.enums.HttpMethod;
import io.github.fishlikewater.raiden.http.core.enums.LogLevel;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

/**
 * {@code OnlyHttpRequestClientTest}
 *
 * @author fishlikewater@126.com
 * @version 1.0.0
 * @since 2024/03/20
 */
public class OnlyHttpRequestClientTest {

    @Before
    public void before() {
        HttpBootStrap.init();
        HttpBootStrap
                .getConfig()
                .setLogLevel(LogLevel.HEADS)
                .setEnableLog(true);
    }

    @Test
    public void testClient() throws IOException, InterruptedException {
        AbstractHttpRequestClient httpRequestClient = HttpBootStrap.getConfig().getHttpClient();
        RequestWrap requestWrap = RequestWrap.builder()
                .httpMethod(HttpMethod.GET)
                .returnType(String.class)
                .typeArgumentClass(String.class)
                .url("https://www.baidu.com")
                .build();
        HttpResponse<Object> response = httpRequestClient.requestSync(requestWrap);
        System.out.println(response.statusCode());
        CompletableFuture<HttpResponse<Object>> async = httpRequestClient.getAsync(requestWrap);
        async.thenAcceptAsync(res -> System.out.println(res.body())).join();
        Assert.assertNotNull(async);
    }
}
