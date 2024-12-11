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
package io.github.fishlikewater.raiden.http.autoconfigure;

import io.github.fishlikewater.raiden.http.core.HttpBootStrap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.net.http.HttpClient;

/**
 * 接口注入
 *
 * @author fishlikewater@126.com
 * @version 1.0.0
 * @since 2023年09月22日 11:12
 **/
@Slf4j
public class HttpContextRefreshedEventListener implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        final String[] namesForType = event.getApplicationContext().getBeanNamesForType(HttpClient.class);
        if (namesForType.length > 0) {
            final HttpClient httpClient = (HttpClient) event.getApplicationContext().getBean(namesForType[0]);
            HttpBootStrap.registerHttpClient("default", httpClient);
        }
    }
}