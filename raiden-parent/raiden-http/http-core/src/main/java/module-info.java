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
/**
 * {@code module-info}
 *
 * @author zhangxiang
 * @since 2024/06/24
 */
module raiden.http.core {
    requires static lombok;
    requires raiden.core;
    requires raiden.json.core;
    requires java.net.http;
    requires io.github.classgraph;
    requires jdk.httpserver;
    requires io.github.resilience4j.circuitbreaker;
    requires io.github.resilience4j.core;
    requires sentinel.core;
    requires com.fasterxml.jackson.core;

    exports io.github.fishlikewater.raiden.http.core;
    exports io.github.fishlikewater.raiden.http.core.enums;
    exports io.github.fishlikewater.raiden.http.core.source;
    exports io.github.fishlikewater.raiden.http.core.annotation;
    exports io.github.fishlikewater.raiden.http.core.interceptor;
    exports io.github.fishlikewater.raiden.http.core.processor;
    exports io.github.fishlikewater.raiden.http.core.constant;
    exports io.github.fishlikewater.raiden.http.core.proxy;
    exports io.github.fishlikewater.raiden.http.core.convert;
    exports io.github.fishlikewater.raiden.http.core.factory;
    exports io.github.fishlikewater.raiden.http.core.client;
    exports io.github.fishlikewater.raiden.http.core.degrade;
    exports io.github.fishlikewater.raiden.http.core.degrade.resilience4j;
    exports io.github.fishlikewater.raiden.http.core.degrade.sentinel;
}