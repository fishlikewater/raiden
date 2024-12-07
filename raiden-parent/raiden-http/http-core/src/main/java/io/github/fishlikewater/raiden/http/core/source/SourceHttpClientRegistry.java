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
package io.github.fishlikewater.raiden.http.core.source;

import java.net.http.HttpClient;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 * HttpClient 注册容器
 * </p>
 *
 * @author fishlikewater@126.com
 * @version 1.0.0
 * @since 2023年09月23日 10:14
 **/
public class SourceHttpClientRegistry {

    private final Map<String, HttpClient> httpClientMap;

    private final List<SourceHttpClientRegister> registrars;

    public SourceHttpClientRegistry(List<SourceHttpClientRegister> registrars) {
        this.registrars = registrars;
        this.httpClientMap = new HashMap<>(16);
    }

    public void init() {
        if (registrars == null) {
            return;
        }
        registrars.forEach(registrar -> registrar.register(this));
    }

    public void register(String name, HttpClient httpClient) {
        httpClientMap.put(name, httpClient);
    }

    public HttpClient get(String name) {
        HttpClient httpClient = httpClientMap.get(name);
        if (Objects.isNull(httpClient)) {
            return httpClientMap.get("default");
        }
        return httpClient;
    }
}
