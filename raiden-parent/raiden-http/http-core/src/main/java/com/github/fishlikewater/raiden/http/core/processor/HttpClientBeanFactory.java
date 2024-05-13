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
package com.github.fishlikewater.raiden.http.core.processor;

import com.github.fishlikewater.raiden.http.core.MethodArgsBean;
import com.github.fishlikewater.raiden.http.core.interceptor.HttpClientInterceptor;

import java.lang.reflect.Method;

/**
 * <p>
 *
 * </p>
 *
 * @author fishlikewater@126.com
 * @since 2023年09月23日 20:25
 * @version 1.0.0
 **/
public interface HttpClientBeanFactory {

    /**
     * 获取方法参数
     * @param methodName 方法名
     * @return 方法参数
     */
    MethodArgsBean getMethodArgsBean(String methodName);

    /**
     * 缓存方法
     * @param method 方法
     */
    void cacheMethod(Method method);

    /**
     * 获取代理对象
     * @param tClass 类
     * @param <T> 泛型
     * @return 代理对象
     */
    <T> T getProxyObject(Class<T> tClass);

    /**
     * 缓存代理对象
     * @param className 类名
     * @param proxyObject 代理对象
     */
    void cacheProxyObject(String className,  Object proxyObject);

    /**
     * 获取拦截器
     * @param interceptorName 拦截器名
     * @return 拦截器
     */
    HttpClientInterceptor getInterceptor(String interceptorName);

    /**
     * 缓存拦截器
     * @param httpClientInterceptor 拦截器
     */
    void setHttpClientInterceptor(HttpClientInterceptor httpClientInterceptor);
}
