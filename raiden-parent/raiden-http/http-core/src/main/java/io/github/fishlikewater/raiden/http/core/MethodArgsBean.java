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

import io.github.fishlikewater.raiden.http.core.enums.DegradeType;
import io.github.fishlikewater.raiden.http.core.enums.HttpMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *
 * </p>
 *
 * @author fishlikewater@126.com
 * @version 1.0.0
 * @since 2023年09月24日 9:37
 **/
@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class MethodArgsBean {

    private String className;

    private String methodName;

    private String serverName;

    private String sourceHttpClientName;

    private List<String> interceptorNames;

    private HttpMethod requestMethod;

    private boolean isForm;

    private Map<String, String> headMap;

    /**
     * 请求协议
     */
    private String protocol;

    /**
     * 类上服务注解 请求前缀
     */
    private String urlPrefix;

    /**
     * 方法上的请求路径
     */
    private String path;

    /**
     * 完整请求路径
     */
    private String url;

    /**
     * url参数
     */
    private Parameter[] urlParameters;

    /**
     * 方法返回类型
     */
    private Class<?> returnType;

    /**
     * 方法返回类型泛型参数
     */
    private Type typeArgument;

    /**
     * 是否是同步请求
     */
    private boolean isSync;

    /**
     * 是否启用熔断
     */
    private boolean degrade;

    /**
     * 错误处理器
     *
     * @since 1.0.2
     */
    private String exceptionProcessorName;

    /**
     * 降级处理
     */
    private String fallbackFactoryName;

    /**
     * 熔断降级配置名称
     */
    private String circuitBreakerConfigName;

    /**
     * 熔断降级类型
     */
    private DegradeType degradeType;

    public void addInterceptorName(String interceptorName) {
        if (this.interceptorNames == null) {
            this.interceptorNames = new ArrayList<>();
        }
        this.interceptorNames.add(interceptorName);
    }
}
