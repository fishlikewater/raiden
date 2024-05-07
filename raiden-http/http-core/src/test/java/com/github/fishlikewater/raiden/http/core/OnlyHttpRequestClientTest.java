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
package com.github.fishlikewater.raiden.http.core;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

/**
 * {@code OnlyHttpRequestClientTest}
 *
 * @author fishlikewater@126.com
 * @since 2024/03/20
 * @version 1.0.0
 */
public class OnlyHttpRequestClientTest {

    @Before
    public void before() {
        HttpBootStrap.init();
        HttpBootStrap
                .getLogConfig()
                .setLogLevel(LogConfig.LogLevel.HEADS)
                .setEnableLog(true);
    }

    @Test
    public void testClient() throws InterruptedException, IOException {
        HttpRequestClient httpRequestClient = HttpBootStrap.getHttpRequestClient();
        String sync = httpRequestClient.getSync("https://www.baidu.com", String.class);
        Assert.assertNotNull(sync);
    }
}
