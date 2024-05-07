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
package com.github.fishlikewater.test.config;

import cn.hutool.core.net.DefaultTrustManager;
import com.github.fishlikewater.raiden.http.core.source.SourceHttpClientRegister;
import com.github.fishlikewater.raiden.http.core.source.SourceHttpClientRegistry;
import org.springframework.stereotype.Component;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.net.http.HttpClient;
import java.time.Duration;

/**
 * {@code SourceHttpClientRegister}
 * 注册自定义的httpclient 在remote接口中可以使用命名指定
 *
 * @author fishlikewater@126.com
 * @since 2024/03/15
 */
@Component
public class CustomerHttpClient implements SourceHttpClientRegister {
    @Override
    public void register(SourceHttpClientRegistry registry) {
        SSLContext ctx;
        try {
            ctx = SSLContext.getInstance("TLS");
            X509TrustManager tm = new DefaultTrustManager();
            ctx.init(null, new TrustManager[]{tm}, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        HttpClient customer = HttpClient.newBuilder()
                .sslContext(ctx)
                .connectTimeout(Duration.ofSeconds(60))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        registry.register("customer", customer);
    }
}
