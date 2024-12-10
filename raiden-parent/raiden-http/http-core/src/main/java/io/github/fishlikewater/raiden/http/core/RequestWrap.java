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

import io.github.fishlikewater.raiden.core.ObjectUtils;
import io.github.fishlikewater.raiden.http.core.degrade.FallbackFactory;
import io.github.fishlikewater.raiden.http.core.enums.DegradeType;
import io.github.fishlikewater.raiden.http.core.enums.HttpMethod;
import io.github.fishlikewater.raiden.http.core.interceptor.HttpInterceptor;
import io.github.fishlikewater.raiden.http.core.processor.ExceptionProcessor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Method;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * {@code RequestWrap}
 * 构建请求所需参数
 *
 * @author zhangxiang
 * @version 1.0.2
 * @since 2024/06/11
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestWrap {

    /**
     * 被调用的方法
     */
    private Method method;

    /**
     * 方法参数
     */
    private Object[] args;

    /**
     * 请求方式
     */
    private HttpMethod httpMethod;

    /**
     * 请求头
     */
    private Map<String, String> headMap;

    /**
     * 返回类型
     */
    private Class<?> returnType;

    /**
     * 返回泛型参数类型
     */
    private Class<?> typeArgumentClass;

    /**
     * 是否是form表单
     */
    private boolean form;

    /**
     * 是否是同步请求
     */
    private boolean sync;

    /**
     * 请求url
     */
    private String url;

    /**
     * 请求参数
     */
    private Map<String, String> paramMap;

    /**
     * 请求体
     */
    private Object bodyObject;

    /**
     * 请求拦截器
     */
    private List<HttpInterceptor> interceptors;

    /**
     * 异常处理器
     */
    private ExceptionProcessor exceptionProcessor;

    /**
     * 文件上传下载
     */
    private MultipartData multipartData;

    /**
     * httpClient
     */
    private HttpClient httpClient;

    /**
     * 重试次数
     */
    private int retryCount;

    /**
     * 是否启用熔断降级
     */
    private boolean degrade;

    /**
     * 熔断降级配置名称
     */
    private String circuitBreakerConfigName;

    /**
     * 降级处理
     */
    private FallbackFactory<?> fallbackFactory;

    /**
     * 熔断降级类型
     */
    private DegradeType degradeType;

    /**
     * 实际请求对象
     */
    private HttpRequest httpRequest;

    public synchronized void addInterceptor(HttpInterceptor interceptor) {
        if (ObjectUtils.isNullOrEmpty(this.interceptors)) {
            this.interceptors = new ArrayList<>();
        }
        this.interceptors.add(interceptor);
    }
}
