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
package io.github.fishlikewater.raiden.http.core.factory;

import io.github.fishlikewater.raiden.http.core.MethodArgsBean;
import io.github.fishlikewater.raiden.http.core.annotation.Degrade;
import io.github.fishlikewater.raiden.http.core.annotation.HttpServer;
import io.github.fishlikewater.raiden.http.core.annotation.Interceptor;
import io.github.fishlikewater.raiden.http.core.degrade.FallbackFactory;
import io.github.fishlikewater.raiden.http.core.interceptor.HttpInterceptor;
import io.github.fishlikewater.raiden.http.core.processor.ExceptionProcessor;

import java.lang.reflect.Method;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author fishlikewater@126.com
 * @version 1.0.0
 * @since 2023年09月23日 20:25
 **/
public interface HttpClientBeanFactory {

    /**
     * 获取方法参数
     *
     * @param methodName 方法名
     * @return 方法参数
     */
    MethodArgsBean getMethodArgsBean(String methodName);

    /**
     * 缓存方法
     *
     * @param method      方法
     * @param httpServer  HttpServer注解
     * @param interceptor Interceptor注解
     * @param degrade     注解
     */
    void cacheMethod(Method method, HttpServer httpServer, Interceptor interceptor, Degrade degrade);

    /**
     * 获取代理对象
     *
     * @param tClass 类
     * @param <T>    泛型
     * @return 代理对象
     */
    <T> T getProxyObject(Class<T> tClass);

    /**
     * 缓存代理对象
     *
     * @param className   类名
     * @param proxyObject 代理对象
     */
    void cacheProxyObject(String className, Object proxyObject);

    /**
     * 获取拦截器
     *
     * @param interceptorName 拦截器名
     * @return 拦截器
     */
    HttpInterceptor getInterceptor(String interceptorName);

    /**
     * 获取拦截器
     *
     * @param interceptorNames 拦截器名
     * @return 拦截器
     */
    List<HttpInterceptor> getInterceptors(List<String> interceptorNames);

    /**
     * 缓存拦截器
     *
     * @param interceptor 拦截器
     */
    void registerHttpClientInterceptor(HttpInterceptor interceptor);

    /**
     * 获取异常处理器
     *
     * @param name 异常处理器名
     * @return 异常处理器
     */
    ExceptionProcessor getExceptionProcessor(String name);

    /**
     * 缓存异常处理器
     *
     * @param exceptionProcessor 异常处理器
     */
    void registerExceptionProcessor(ExceptionProcessor exceptionProcessor);

    /**
     * 获取fallback工厂
     *
     * @param name fallback工厂名
     * @return fallback工厂
     */
    FallbackFactory<?> getFallbackFactory(String name);

    /**
     * 缓存fallback工厂
     *
     * @param fallbackFactory fallback工厂
     */
    void registerFallbackFactory(FallbackFactory<?> fallbackFactory);
}
