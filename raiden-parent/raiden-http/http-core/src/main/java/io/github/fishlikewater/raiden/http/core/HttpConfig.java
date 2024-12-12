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
import io.github.fishlikewater.raiden.http.core.degrade.resilience4j.CircuitBreakerConfigRegistry;
import io.github.fishlikewater.raiden.http.core.degrade.sentinel.SentinelDegradeRuleRegistry;
import io.github.fishlikewater.raiden.http.core.enums.DegradeType;
import io.github.fishlikewater.raiden.http.core.enums.LogLevel;
import io.github.fishlikewater.raiden.http.core.factory.HttpClientBeanFactory;
import io.github.fishlikewater.raiden.http.core.interceptor.LogHttpInterceptor;
import io.github.fishlikewater.raiden.http.core.interceptor.PredRequestInterceptor;
import io.github.fishlikewater.raiden.http.core.processor.HttpClientProcessor;
import io.github.fishlikewater.raiden.http.core.proxy.InterfaceProxy;
import io.github.fishlikewater.raiden.http.core.source.SourceHttpClientRegistry;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * HttpConfig
 *
 * @author zhangxiang
 * @version 1.0.8
 * @since 2024/11/10
 **/
@Data
@Accessors(chain = true)
public class HttpConfig {

    /**
     * 是否自行管理代理容器
     */
    private boolean selfManager;

    /**
     * 最大重试次数
     */
    private int maxRetryCount;

    /**
     * 重试间隔时间
     */
    private long retryInterval;

    /**
     * 是否开启日志
     */
    private boolean enableLog;

    /**
     * 是否开全局启熔断降级
     */
    private boolean enableDegrade;

    /**
     * 全局熔断降级类型
     */
    private DegradeType degradeType;

    /**
     * 接口代理
     */
    private InterfaceProxy interfaceProxy;

    /**
     * httpClient 请求客户端
     */
    private AbstractHttpRequestClient httpClient;

    /**
     * httpClient Bean处理器工厂
     */
    private HttpClientBeanFactory httpClientBeanFactory;

    /**
     * httpClient 处理器
     */
    private HttpClientProcessor httpClientProcessor;

    /**
     * httpClient 注册中心
     */
    private SourceHttpClientRegistry sourceHttpClientRegistry;

    /**
     * 日志级别
     */
    private LogLevel logLevel = LogLevel.BASIC;

    /**
     * 全局预请求拦截器
     */
    private PredRequestInterceptor predRequestInterceptor;

    /**
     * 全局日志拦截器
     */
    private LogHttpInterceptor logInterceptor = new LogHttpInterceptor();

    /**
     * resilience4j熔断注册器
     */
    private CircuitBreakerConfigRegistry breakerConfigRegistry;

    /**
     * sentinel熔断注册器
     */
    private SentinelDegradeRuleRegistry sentDegradeRuleRegistry;
}
